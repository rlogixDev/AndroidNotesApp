package com.noteapp.services

import com.google.gson.JsonObject
import com.noteapp.dataclass.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

interface IAddressManager {
    public suspend fun getAddress() : StateFlow<ArrayList<Map<String, AddressResponse>>?>
}

class ServiceManager
@Inject constructor(): IAddressManager {
    val baseURL = "https://rlogixx-33270-default-rtdb.firebaseio.com/"
    val serverError = "Unable to get response from server."
    // Interface to be used as Retrofit service.
    // We're using Dog.ceo public API.
    val apiClient: APIClient = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(APIClient::class.java)
    override suspend fun getAddress(): StateFlow<ArrayList<Map<String, AddressResponse>>?> {
        val loginResponse: MutableStateFlow<ArrayList<Map<String, AddressResponse>>?> = MutableStateFlow(null)
        val response = apiClient.getAddress()
        val callBack = object : Callback<ArrayList<Map<String, AddressResponse>>?> {
            override fun onResponse(
                call: Call<ArrayList<Map<String, AddressResponse>>?>,
                response: Response<ArrayList<Map<String, AddressResponse>>?>
            ) {
                loginResponse.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<Map<String, AddressResponse>>?>, t: Throwable) {
                val apiResponseLogin =  null
                loginResponse.value = apiResponseLogin
            }
        }
        response.enqueue(callBack)
        return loginResponse
    }
}