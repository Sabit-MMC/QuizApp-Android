package com.example.quizapp.data.network

import com.example.quizapp.tools.DataStoreHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val dataStoreHelper: DataStoreHelper
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = runBlocking {
            dataStoreHelper.getAccessToken().first()
        }

        val newRequest = if (
            accessToken.isNullOrEmpty() ||
            originalRequest.url.encodedPath.contains("/refresh")
        ) {
            originalRequest
        } else {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        }

        return chain.proceed(newRequest)
    }
}