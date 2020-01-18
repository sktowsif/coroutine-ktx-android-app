package com.example.profiles.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)

sealed class Outcome<T> {

    data class Progress<T>(var loading: Boolean) : Outcome<T>()
    data class Success<T>(var data: T) : Outcome<T>()
    data class Failure<T>(val e: Throwable) : Outcome<T>()

    companion object {

        fun <T> loading(isLoading: Boolean): Outcome<T> =
            Progress(isLoading)

        fun <T> success(data: T): Outcome<T> =
            Success(data)

        fun <T> failure(e: Throwable): Outcome<T> =
            Failure(e)
    }
}