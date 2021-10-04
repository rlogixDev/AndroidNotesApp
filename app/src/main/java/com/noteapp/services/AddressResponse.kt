package com.noteapp.services

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Keep
class AddressResponse {
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("state")
    @Expose
    var state: String? = null
}