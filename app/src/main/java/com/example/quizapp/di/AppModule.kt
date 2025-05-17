package com.example.quizapp.di

import android.content.Context
import android.util.Log
import com.example.quizapp.data.network.AuthInterceptor
import com.example.quizapp.data.network.TokenAuthenticator
import com.example.quizapp.data.repository.auth.AuthRepository
import com.example.quizapp.data.repository.quiz.QuizRepository
import com.example.quizapp.data.services.auth.AuthServices
import com.example.quizapp.data.services.quiz.ApiService
import com.example.quizapp.tools.DataStoreHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        dataStoreHelper: DataStoreHelper,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(dataStoreHelper))
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        @ApplicationContext context: Context,
        dataStoreHelper: DataStoreHelper
    ): TokenAuthenticator {
        return TokenAuthenticator(
            context = context,
            dataStoreHelper = dataStoreHelper
        )
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient).build()

    @Provides
    @Singleton
    fun provideQuizService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthServices =
        retrofit.create(AuthServices::class.java)

    @Provides
    @Singleton
    fun provideQuizRepository(apiService: ApiService): QuizRepository = QuizRepository(apiService)

    @Provides
    @Singleton
    fun provideAuthRepository(authServices: AuthServices): AuthRepository =
        AuthRepository(authServices)
}