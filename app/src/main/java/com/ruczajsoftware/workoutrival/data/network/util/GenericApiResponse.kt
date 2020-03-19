package com.ruczajsoftware.workoutrival.data.network.util

import android.util.Log
import com.ruczajsoftware.workoutrival.util.ErrorHandling.Companion.ERROR_401
import com.ruczajsoftware.workoutrival.util.ErrorHandling.Companion.ERROR_403
import com.ruczajsoftware.workoutrival.util.ErrorHandling.Companion.ERROR_404
import com.ruczajsoftware.workoutrival.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.ruczajsoftware.workoutrival.util.ErrorHandling.Companion.FAILED_TO_CONNECT_TO_SERVER
import retrofit2.Response

@Suppress("unused") // T is used in extending classes
sealed class GenericApiResponse<T> {

    companion object {
        private val TAG: String = "AppDebug"


        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            error.message?.let { message ->
                if (message.contains("failed to connect"))
                    return ApiErrorResponse(
                        FAILED_TO_CONNECT_TO_SERVER
                    )
            }
            return ApiErrorResponse(
                error.message ?: ERROR_UNKNOWN
            )
        }

        fun <T> create(response: Response<T>): GenericApiResponse<T> {

            Log.d(TAG, "GenericApiResponse: response: ${response}")
            Log.d(TAG, "GenericApiResponse: raw: ${response.raw()}")
            Log.d(TAG, "GenericApiResponse: headers: ${response.headers()}")
            Log.d(TAG, "GenericApiResponse: message: ${response.message()}")

            val body = response.body()
            return when (response.code()) {
                in 200..300 -> if (body != null) ApiSuccessResponse(
                    body = body
                ) else ApiEmptyResponse()
                401 -> ApiErrorResponse(
                    ERROR_401
                )
                403 -> ApiErrorResponse(
                    ERROR_403
                )
                404 -> ApiErrorResponse(
                    ERROR_404
                )
                else -> ApiErrorResponse(
                    ERROR_UNKNOWN
                )
            }
        }
    }

}

class ApiEmptyResponse<T> : GenericApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : GenericApiResponse<T>() {}

data class ApiErrorResponse<T>(val errorMessage: String) : GenericApiResponse<T>()
