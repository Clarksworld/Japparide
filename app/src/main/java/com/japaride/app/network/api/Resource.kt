package com.japaride.app.network.api

import okhttp3.ResponseBody

sealed class Resource<out T>{


//    data class Loading<out T>(val value: T): Resource<T>()



    data class Success<out T>(val value: T): Resource<T>()


    data class Error(
        val isNetWorkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ): Resource<Nothing>()

    object Loading: Resource<Nothing>()
}