package com.example.quizapp.data.network

import android.content.Context
import android.content.Intent
import com.example.quizapp.data.model.auth.RefreshRequest
import com.example.quizapp.data.model.auth.RefreshTokenResponse
import com.example.quizapp.data.services.auth.AuthServices
import com.example.quizapp.presentation.auth.MainActivity
import com.example.quizapp.tools.DataStoreHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

class TokenAuthenticator(
    private val context: Context,
    private val dataStoreHelper: DataStoreHelper
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return synchronized(this) {
            runBlocking {
                val oldToken = dataStoreHelper.getAccessToken().first()
                refreshToken(dataStoreHelper)?.let {
                    if(it.isSuccessful){
                        val newToken = it.body()?.accessToken
                        if (newToken.isNullOrEmpty() || newToken == oldToken) {
                            return@runBlocking null
                        }
                        dataStoreHelper.clearAccessToken()
                        dataStoreHelper.saveAccessToken(newToken)
                        return@runBlocking newRequest(response.request,newToken)
                    }else if(it.code() == 401) {
                        dataStoreHelper.clearCredentials()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        return@runBlocking null
                    }else {
                        return@runBlocking null
                    }
                }
                return@runBlocking null
            }
        }
    }

    private suspend fun refreshToken(dataStoreHelper: DataStoreHelper): retrofit2.Response<RefreshTokenResponse>? {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json;charset=utf-8")

                val request = requestBuilder.build()

                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(AuthServices::class.java)

        val refreshToken = runBlocking { dataStoreHelper.getRefreshToken().first() }
        val userId = runBlocking { dataStoreHelper.getUserId().first() }


        if (refreshToken?.isEmpty() == true) {
            return null
        }

        return service.refresh(RefreshRequest(userId ?: "","$refreshToken"))
    }

    private fun newRequest(request: Request, accessToken: String): Request {
        return request.newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
    }
}
