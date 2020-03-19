package com.ruczajsoftware.workoutrival.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ruczajsoftware.workoutrival.data.repository.auth.AuthRepository
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthStateEvent
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthStateEvent.*
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthViewState
import com.ruczajsoftware.workoutrival.ui.auth.state.LoginFields
import com.ruczajsoftware.workoutrival.ui.auth.state.RegistrationFields
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

    fun setAuthToken(authToken: Boolean) {
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
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}