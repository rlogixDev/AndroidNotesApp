package com.noteapp.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

interface ILoginServiceManage {
    public suspend fun login(number: String, password: String) : Flow<LoginResponse?>
}

class ServiceManager
@Inject constructor(): ILoginServiceManage {
    val baseURL = "https://archaeogroup.com/allen/api/"
    val serverError = "Unable to get response from server."
    // Interface to be used as Retrofit service.
    // We're using Dog.ceo public API.
    val apiClient: APIClient = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(APIClient::class.java)
    override suspend fun login(number: String, password: String): Flow<LoginResponse?> {
        val loginResponse: MutableStateFlow<LoginResponse?> = MutableStateFlow(null)
        val response = apiClient.login(number, password)
        val callBack = object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                loginResponse.value = response.body()
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                val apiResponseLogin =  LoginResponse()
                loginResponse.value = apiResponseLogin
            }
        }
        response.enqueue(callBack)
        return loginResponse
    }
}