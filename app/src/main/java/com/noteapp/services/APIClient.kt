package com.noteapp.services

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIClient {
    @FormUrlEncoded
    @POST("user/user_login")
    fun login(@Field("number") number: String,
              @Field("password") password: String): Call<LoginResponse>
}