package com.ruczajsoftware.workoutrival.data.network

import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.data.model.RegisterRequest
import com.ruczajsoftware.workoutrival.data.model.ServerStatus
import com.ruczajsoftware.workoutrival.data.network.util.GenericApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServicesApi {

    @GET("actuator/health")
    fun isServerAvailable(): LiveData<GenericApiResponse<ServerStatus>>

    @GET("workoutRival/users/login")
    fun login(
        @Query("login") login: String,
        @Query("password") password: String
    ): LiveData<GenericApiResponse<Boolean>>

    @POST("workoutRival/users")
    fun register(@Body registerRequest: RegisterRequest): LiveData<GenericApiResponse<String>>
}