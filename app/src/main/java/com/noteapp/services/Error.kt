package com.noteapp.services

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class Error {
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("errorCode")
    @Expose
    var errorCode: Int? = null

}