package com.example.quizapp.tools

import com.google.gson.Gson
import retrofit2.Response
import java.lang.Exception

fun Response<*>.toErrorResponse(): String {
    val errorBodyString = this.errorBody()?.string()
    return errorBodyString?.let {
        try {
            val errorResponse = Gson().fromJson(it, ErrorResponse::class.java)
            errorResponse.message
        } catch (e: Exception) {
            it
        }
    } ?: "Unknown error"
}

data class ErrorResponse(
    val code: Int,
    val description: String,
    val message: String
)