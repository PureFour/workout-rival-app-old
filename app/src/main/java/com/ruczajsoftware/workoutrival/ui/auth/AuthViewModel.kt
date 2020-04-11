package com.ruczajsoftware.workoutrival.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ruczajsoftware.workoutrival.data.db.entity.AuthToken
import com.ruczajsoftware.workoutrival.data.repository.auth.AuthRepository
import com.ruczajsoftware.workoutrival.ui.auth.state.*
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthStateEvent.*
import com.ruczajsoftware.workoutrival.util.DataState
import com.ruczajsoftware.workoutrival.util.Loading
import com.ruczajsoftware.workoutrival.util.viewmodel.BaseViewModel

class AuthViewModel(private val authRepository: AuthRepository) :
    BaseViewModel<AuthStateEvent, AuthViewState>() {


    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when (stateEvent) {
            is LoginAttemptEvent -> {
                return authRepository.attemptLogin(
                    stateEvent.email,
                    stateEvent.password
                )
            }

            is RegisterAttemptEvent -> {
                return authRepository.attemptRegistration(
                    stateEvent.email,
                    stateEvent.username,
                    stateEvent.password,
                    stateEvent.confirm_password
                )
            }

            is CheckPreviousAuthEvent -> {
                return authRepository.checkPreviousAuthUser()
            }

            is RequestNewPinEvent -> {
                return authRepository.attemptRequestNewPin(stateEvent.email)
            }

            is UpdatePasswordEvent -> {
                return authRepository.attemptUpdatePassword(
                    stateEvent.email,
                    stateEvent.newPassword,
                    stateEvent.confirmPassword,
                    stateEvent.pin
                )
            }

            is None -> {
                return liveData {
                    emit(
                        DataState(
                            null,
                            Loading(false),
                            null
                        )
                    )
                }
            }
        }
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        if (update.registrationFields == registrationFields) {
            return
        }
        update.registrationFields = registrationFields
        setViewState(update)
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        if (update.loginFields == loginFields) {
            return
        }
        update.loginFields = loginFields
        setViewState(update)
    }

    fun setEmailOnForgotPasswordFragment(email: String) {
        val update = getCurrentViewStateOrNew()
        if (update.forgotPasswordEmail == email)
            return
        update.forgotPasswordEmail = email
        setViewState(update)
    }

    fun setUpdatePasswordFields(passwordFields: UpdatePasswordFields) {
        val update = getCurrentViewStateOrNew()
        if (update.updatePasswordFields == passwordFields) {
            return
        }
        update.updatePasswordFields = passwordFields
        setViewState(update)
    }

    fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken == authToken) {
            return
        }
        update.authToken = authToken
        setViewState(update)
    }

    fun cancelActiveJobs() {
        handlePendingData()
        authRepository.cancelActiveJobsOver()
    }

    fun handlePendingData() {
        setStateEvent(None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}