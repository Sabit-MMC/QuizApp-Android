package com.example.quizapp.data.network

import kotlinx.io.IOException


sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall())
    } catch (e: io.ktor.client.plugins.ClientRequestException) {
        NetworkResult.Error("Client Error: ${e.response.status.description}", e.response.status.value)
    } catch (e: io.ktor.client.plugins.ServerResponseException) {
        NetworkResult.Error("Server Error: ${e.response.status.description}", e.response.status.value)
    } catch (e: IOException) {
        NetworkResult.Error("Network Error: Check your connection")
    } catch (e: Exception) {
        NetworkResult.Error("Unknown Error: ${e.localizedMessage}")
    }
}