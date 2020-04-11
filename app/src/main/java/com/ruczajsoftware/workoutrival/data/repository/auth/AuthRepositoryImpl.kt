package com.ruczajsoftware.workoutrival.data.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.data.db.AuthTokenDao
import com.ruczajsoftware.workoutrival.data.db.entity.AuthToken
import com.ruczajsoftware.workoutrival.data.db.entity.CURRENT_TOKEN_INDEX
import com.ruczajsoftware.workoutrival.data.network.ServicesApi
import com.ruczajsoftware.workoutrival.data.network.util.ApiSuccessResponse
import com.ruczajsoftware.workoutrival.data.network.util.GenericApiResponse
import com.ruczajsoftware.workoutrival.data.repository.JobManager
import com.ruczajsoftware.workoutrival.data.repository.NetworkBoundResource
import com.ruczajsoftware.workoutrival.data.servicesModel.AuthResponse
import com.ruczajsoftware.workoutrival.data.servicesModel.LoginRequest
import com.ruczajsoftware.workoutrival.data.servicesModel.RegisterRequest
import com.ruczajsoftware.workoutrival.data.servicesModel.UpdatePasswordRequest
import com.ruczajsoftware.workoutrival.session.SessionManager
import com.ruczajsoftware.workoutrival.ui.auth.state.*
import com.ruczajsoftware.workoutrival.util.DataState
import com.ruczajsoftware.workoutrival.util.Response
import com.ruczajsoftware.workoutrival.util.ResponseType
import com.ruczajsoftware.workoutrival.util.SuccessHandling.Companion.NO_ERROR
import com.ruczajsoftware.workoutrival.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.Job


class AuthRepositoryImpl(
    private val servicesApi: ServicesApi,
    private val sessionManager: SessionManager,
    private val authTokenDao: AuthTokenDao
) : JobManager("AuthRepository"),
    AuthRepository {

    private val TAG: String = "AppDebug"

    override fun attemptLogin(
        email: String,
        password: String
    ): LiveData<DataState<AuthViewState>> {
        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if (loginFieldErrors != LoginFields.LoginError.none()) {
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<AuthResponse, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<AuthResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")

                val result = authTokenDao.insert(
                    AuthToken(
                        response.body?.token
                    )
                )

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body?.token)
                        )
                    )
                )
            }


            override fun createCall(): LiveData<GenericApiResponse<AuthResponse>> {
                return servicesApi.login(LoginRequest(email = email, password = password))
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

        return object : NetworkBoundResource<AuthResponse, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<AuthResponse>) {
                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body?.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<AuthResponse>> {
                return servicesApi.register(RegisterRequest(email, username, password))
            }

            override fun setJob(job: Job) {
                addJob("attemptRegistration", job)
            }
        }.asLiveData()
    }

    override fun attemptRequestNewPin(email: String): LiveData<DataState<AuthViewState>> {
        val emailFieldError = email.isValidForInput()
        if (emailFieldError != NO_ERROR) {
            return returnErrorResponse(emailFieldError, ResponseType.Dialog())
        }
        return object : NetworkBoundResource<Void, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {
                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            isRequestPinSuccess = true
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return servicesApi.requestNewPin(email)
            }

            override fun setJob(job: Job) {
                addJob("attemptRequestNewPin", job)
            }

        }.asLiveData()
    }

    override fun attemptUpdatePassword(
        email: String,
        newPassword: String,
        confirmPassword: String,
        pin: String
    ): LiveData<DataState<AuthViewState>> {
        val updatePasswordFieldError = UpdatePasswordFields(
            newPassword,
            confirmPassword,
            email,
            pin
        ).isValidForUpdatePassword()
        if (updatePasswordFieldError != UpdatePasswordFields.UpdatePasswordError.none()) {
            return returnErrorResponse(updatePasswordFieldError, ResponseType.Dialog())
        }
        return object : NetworkBoundResource<Void, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {
                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            updatePasswordFields = UpdatePasswordFields(
                                newPassword,
                                confirmPassword,
                                email,
                                pin
                            ),
                            isUpdatePasswordSuccess = true
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return servicesApi.updatePassword(UpdatePasswordRequest(email, newPassword, pin))
            }

            override fun setJob(job: Job) {
                addJob("attemptRequestNewPin", job)
            }

        }.asLiveData()
    }

    override fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>> {

        return object : NetworkBoundResource<Void, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            false,
            false,
            false
        ) {

            override suspend fun createCacheRequestAndReturn() {

                authTokenDao.searchByPk(CURRENT_TOKEN_INDEX).let { authToken ->
                    if (authToken != null) {
                        if (authToken.token != null) {
                            onCompleteJob(
                                DataState.data(
                                    AuthViewState(authToken = authToken)
                                )
                            )
                            return
                        }
                    }
                    onCompleteJob(
                        DataState.data(
                            null,
                            Response(
                                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None()
                            )
                        )
                    )
                }
            }

            override fun setJob(job: Job) {
                addJob("checkPreviousAuthUser", job)
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










