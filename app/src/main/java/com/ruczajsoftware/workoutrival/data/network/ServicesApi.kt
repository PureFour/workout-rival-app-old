package com.ruczajsoftware.workoutrival.data.network

import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.data.network.util.GenericApiResponse
import com.ruczajsoftware.workoutrival.data.servicesModel.*
import retrofit2.http.*

interface ServicesApi {

    @GET("actuator/health")
    fun isServerAvailable(): LiveData<GenericApiResponse<ServerStatus>>

    @POST("workoutRival/users/signIn")
    fun login(@Body loginRequest: LoginRequest): LiveData<GenericApiResponse<AuthResponse>>

    @POST("workoutRival/users/signUp")
    fun register(@Body registerRequest: RegisterRequest): LiveData<GenericApiResponse<AuthResponse>>

    @POST("workoutRival/users/resetPassword")
    fun requestNewPin(@Query("email") email: String): LiveData<GenericApiResponse<Void>>

    @PUT("workoutRival/users/password")
    fun updatePassword(@Body updatePasswordRequest: UpdatePasswordRequest): LiveData<GenericApiResponse<Void>>
}