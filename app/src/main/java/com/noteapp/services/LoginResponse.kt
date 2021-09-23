package com.noteapp.services

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Keep
class LoginResponse {
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null
    @SerializedName("error")
    @Expose
    var error: Error? = null
    @SerializedName("responseData")
    @Expose
    var responseData: Objects? = null
}