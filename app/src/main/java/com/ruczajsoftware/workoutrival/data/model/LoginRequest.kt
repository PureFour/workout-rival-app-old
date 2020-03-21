package com.ruczajsoftware.workoutrival.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    val usermname: String,
    @SerializedName("password")
    val password: String
)