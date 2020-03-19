package com.ruczajsoftware.workoutrival.data.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String
)