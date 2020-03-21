package com.ruczajsoftware.workoutrival.data.repository.auth

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.data.model.LoginRequest
import com.ruczajsoftware.workoutrival.data.model.RegisterRequest
import com.ruczajsoftware.workoutrival.data.network.ServicesApi
import com.ruczajsoftware.workoutrival.data.network.util.ApiSuccessResponse
import com.ruczajsoftware.workoutrival.data.network.util.GenericApiResponse
import com.ruczajsoftware.workoutrival.data.repository.JobManager
import com.ruczajsoftware.workoutrival.data.repository.NetworkBoundResource
import com.ruczajsoftware.workoutrival.session.SessionManager
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthViewState
import com.ruczajsoftware.workoutrival.ui.auth.state.LoginFields
import com.ruczajsoftware.workoutrival.ui.auth.state.RegistrationFields
import com.ruczajsoftware.workoutrival.util.AbsentLiveData
import com.ruczajsoftware.workoutrival.util.DataState
import com.ruczajsoftware.workoutrival.util.Response
import com.ruczajsoftware.workoutrival.util.ResponseType
import kotlinx.coroutines.Job


class AuthRepositoryImpl(
    private val servicesApi: ServicesApi,
    private val sessionManager: SessionManager,
    private val context: Context
) : JobManager("AuthRepository"),
    AuthRepository {

    private val TAG: String = "AppDebug"


    override fun isConnectedToTheInternet(): Boolean {
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            return cm.activeNetworkInfo.isConnected
        } catch (e: Exception) {
            Log.e(TAG, "isConnectedToTheInternet: ${e.message}")
        }
        return false
    }

    override fun attemptLogin(
        username: String,
        password: String
    ): LiveData<DataState<AuthViewState>> {

        val loginFieldErrors = LoginFields(username, password).isValidForLogin()
        if (!loginFieldErrors.equals(LoginFields.LoginError.none())) {
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<Boolean, Any, AuthViewState>(
            isConnectedToTheInternet(),
            true,
            true,
            false
        ) {

            // Ignore
            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Boolean>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = response.body //tu zamiast flagi bedzie ustawiany token
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<Boolean>> {
                return servicesApi.login(LoginRequest(usermname = username, password = password))
            }

            override fun setJob(job: Job) {
                addJob("attemptLogin", job)
            }

        }.asLiveData()
    }

    override fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {

        val registrationFieldErrors =
            RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if (!registrationFieldErrors.equals(RegistrationFields.RegistrationError.none())) {
            return returnErrorResponse(registrationFieldErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<String, Any, AuthViewState>(
            isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            // Ignore
            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<String>) {

                Log.d(TAG, "handleApiSuccessResponse: ${response}")

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = true //tu zamiast flagi bedzie ustawiany token
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<String>> {
                return servicesApi.register(RegisterRequest(email, username, password))
            }

            override fun setJob(job: Job) {
                addJob("attemptRegistration", job)
            }

        }.asLiveData()
    }


    private fun returnErrorResponse(
        errorMessage: String,
        responseType: ResponseType
    ): LiveData<DataState<AuthViewState>> {
        Log.d(TAG, "returnErrorResponse: ${errorMessage}")

        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }

    override fun cancelActiveJobsOver() {
        super.cancelActiveJobs()
    }
}










