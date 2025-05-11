package com.example.quizapp.tools

data class LCE<T>(
    var status: Status? = null,
    var data: T? = null,
    var throwable: Throwable? = null,
    var code: Int? = null,
    var errorMessage: String? = null
) {

    companion object {

        fun <T> loading(data: T?=null): LCE<T> {
            return LCE(
                status = Status.LOADING,
                data = data
            )
        }

        fun <T> content(data: T?): LCE<T> {
            return LCE(
                status = Status.CONTENT,
                data = data
            )
        }

        fun <T> error(throwable: Throwable? = null, code: Int? = null,errorMessage: String? = null): LCE<T> {
            return LCE(
                status = Status.ERROR,
                throwable = throwable,
                code = code,
                errorMessage = errorMessage
            )
        }
    }
}

enum class Status {
    LOADING, CONTENT, ERROR;
}

