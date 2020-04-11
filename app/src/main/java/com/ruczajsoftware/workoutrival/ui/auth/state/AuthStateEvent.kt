package com.ruczajsoftware.workoutrival.ui.auth.state

sealed class AuthStateEvent {

    data class LoginAttemptEvent(
        val email: String,
        val password: String
    ) : AuthStateEvent()

    data class RegisterAttemptEvent(
        val email: String,
        val username: String,
        val password: String,
        val confirm_password: String
    ) : AuthStateEvent()

    data class RequestNewPinEvent(
        val email: String
    ) : AuthStateEvent()

    data class UpdatePasswordEvent(
        val email: String,
        val newPassword: String,
        val confirmPassword: String,
        val pin: String
    ) : AuthStateEvent()

    object CheckPreviousAuthEvent : AuthStateEvent()

    object None : AuthStateEvent()
}