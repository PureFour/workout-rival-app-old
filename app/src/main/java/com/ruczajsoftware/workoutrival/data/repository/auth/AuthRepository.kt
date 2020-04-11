package com.ruczajsoftware.workoutrival.data.repository.auth

import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthViewState
import com.ruczajsoftware.workoutrival.util.DataState

interface AuthRepository {
    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>>
    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>>

    fun cancelActiveJobsOver()

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>>

    fun attemptRequestNewPin(email: String): LiveData<DataState<AuthViewState>>

    fun attemptUpdatePassword(
        email: String,
        newPassword: String,
        confirmPassword: String,
        pin: String
    ): LiveData<DataState<AuthViewState>>
}
