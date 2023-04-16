package com.example.mediaplayer

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.POST

interface API {

    @POST
    suspend fun postrequest(): Call<Any>

    companion object {

        const val TAG = "logs"
        const val baseURL = "https://oauth.vk.com/authorize"

        fun createRequest() : API {
            val request = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build()
            return request.create(API::class.java)
        }
    }

}