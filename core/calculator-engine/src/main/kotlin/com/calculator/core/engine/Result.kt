package com.calculator.core.engine

sealed interface Result<T> {
    data class Success<T>(val value: T) : Result<T>
    data class Failure(val error: Throwable) : Result<Nothing>

    companion object {
        fun <T> success(value: T): Result<T> = Success(value)
        @Suppress("UNCHECKED_CAST")
        fun <T> failure(error: Throwable): Result<T> = Failure(error) as Result<T>
    }

    fun isSuccess(): Boolean = this is Success<*>
    fun isFailure(): Boolean = this is Failure

    fun getOrThrow(): T = when (this) {
        is Success -> value
        is Failure -> throw error
    }

    fun getOrDefault(default: T): T = when (this) {
        is Success -> value
        is Failure -> default
    }

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    val exceptionOrNull: Throwable?
        get() = when (this) {
            is Failure -> error
            is Success -> null
        }
}