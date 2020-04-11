package com.ruczajsoftware.workoutrival.ui.auth.state

import android.os.Parcelable
import com.ruczajsoftware.workoutrival.data.db.entity.AuthToken
import com.ruczajsoftware.workoutrival.util.SuccessHandling.Companion.NO_ERROR
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthViewState(
    var registrationFields: RegistrationFields? = RegistrationFields(),
    var loginFields: LoginFields? = LoginFields(),
    var forgotPasswordEmail: String? = null,
    var updatePasswordFields: UpdatePasswordFields? = UpdatePasswordFields(),
    var authToken: AuthToken? = null,
    var isRequestPinSuccess: Boolean? = false,
    var isUpdatePasswordSuccess: Boolean? = false
) : Parcelable


@Parcelize
data class RegistrationFields(
    var registrationEmail: String? = null,
    var registrationUsername: String? = null,
    var registrationPassword: String? = null,
    var registrationConfirmPassword: String? = null
) : Parcelable {

    class RegistrationError {
        companion object {

            fun mustFillAllFields(): String {
                return "All fields are required."
            }

            fun passwordsDoNotMatch(): String {
                return "Passwords must match."
            }

            fun none(): String {
                return NO_ERROR
            }

        }
    }

    fun isValidForRegistration(): String {
        if (registrationEmail.isNullOrEmpty()
            || registrationUsername.isNullOrEmpty()
            || registrationPassword.isNullOrEmpty()
            || registrationConfirmPassword.isNullOrEmpty()
        ) {
            return RegistrationError.mustFillAllFields()
        }

        if (!registrationPassword.equals(registrationConfirmPassword)) {
            return RegistrationError.passwordsDoNotMatch()
        }
        return RegistrationError.none()
    }
}

@Parcelize
data class LoginFields(
    var loginEmail: String? = null,
    var loginPassword: String? = null
) : Parcelable {
    class LoginError {

        companion object {

            fun mustFillAllFields(): String {
                return "You can't login without an email and password."
            }

            fun none(): String {
                return NO_ERROR
            }
        }
    }

    fun isValidForLogin(): String {

        if (loginEmail.isNullOrEmpty()
            || loginPassword.isNullOrEmpty()
        ) {
            return LoginError.mustFillAllFields()
        }
        return LoginError.none()
    }

    override fun toString(): String {
        return "LoginState(email=$loginEmail, password=$loginPassword)"
    }
}

@Parcelize
data class UpdatePasswordFields(
    var newPassword: String? = null,
    var confirmPassword: String? = null,
    var email: String? = null,
    var pin: String? = null
) : Parcelable {

    class UpdatePasswordError {
        companion object {

            fun mustFillAllFields(): String {
                return "All fields are required."
            }

            fun passwordsDoNotMatch(): String {
                return "Passwords must match."
            }

            fun none(): String {
                return NO_ERROR
            }

        }
    }

    fun isValidForUpdatePassword(): String {
        if (newPassword.isNullOrEmpty()
            || confirmPassword.isNullOrEmpty()
            || email.isNullOrEmpty()
            || pin.isNullOrEmpty()
        ) {
            return UpdatePasswordError.mustFillAllFields()
        }

        if (!newPassword.equals(confirmPassword)) {
            return UpdatePasswordError.passwordsDoNotMatch()
        }
        return UpdatePasswordError.none()
    }
}


fun String.isValidForInput(): String {
    return if (this.isNullOrEmpty()) {
        "You can't send empty value"
    } else NO_ERROR
}