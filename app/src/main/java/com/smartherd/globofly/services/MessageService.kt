package com.smartherd.globofly.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface MessageService {

    @GET
    fun getMessage(@Url anotherUrL:String): Call<String>
}