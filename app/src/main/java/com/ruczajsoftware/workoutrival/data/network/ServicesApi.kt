package com.ruczajsoftware.workoutrival.data.network

import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.data.model.LoginRequest
import com.ruczajsoftware.workoutrival.data.model.RegisterRequest
import com.ruczajsoftware.workoutrival.data.model.ServerStatus
import com.ruczajsoftware.workoutrival.data.network.util.GenericApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServicesApi {

    @GET("actuator/health")
    fun isServerAvailable(): LiveData<GenericApiResponse<ServerStatus>>

    @POST("workoutRival/users/login")
    fun login(@Body loginRequest: LoginRequest): LiveData<GenericApiResponse<Boolean>>

    @POST("workoutRival/users/signUp")
    fun register(@Body registerRequest: RegisterRequest): LiveData<GenericApiResponse<String>>
}