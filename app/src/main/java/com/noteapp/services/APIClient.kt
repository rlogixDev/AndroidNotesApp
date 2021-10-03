package com.noteapp.services

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*
import kotlin.collections.ArrayList

interface APIClient {
    @GET("zipcode_details.json")
    fun getAddress(): Call<java.util.ArrayList<Map<String, AddressResponse>>>
}