package com.ruczajsoftware.workoutrival.data.model

import com.google.gson.annotations.SerializedName

data class ServerStatus(
    @SerializedName("status")
    val status: String
)