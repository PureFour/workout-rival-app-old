package com.ruczajsoftware.workoutrival.data.servicesModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthResponse(
    @SerializedName("token")
    var token: String? = null
) : Parcelable