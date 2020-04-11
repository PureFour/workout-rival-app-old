package com.ruczajsoftware.workoutrival.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ruczajsoftware.workoutrival.R
import com.ruczajsoftware.workoutrival.internal.viewModel
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthStateEvent
import com.ruczajsoftware.workoutrival.ui.auth.state.UpdatePasswordFields
import kotlinx.android.synthetic.main.fragment_have_pin.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class HavePinFragment : Fragment(R.layout.fragment_have_pin), KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.cancelActiveJobs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        setupListeners()
    }

    private fun setupListeners() {
        loginButton.setOnClickListener { backToLoginFragment() }
        requestNewPinButton.setOnClickListener { backToForgotPasswordFragment() }
        resetPasswordButton.setOnClickListener { resetPassword() }
    }

    private fun resetPassword() {
        saveUpdatePasswordFields()
        viewModel.setStateEvent(
            AuthStateEvent.UpdatePasswordEvent(
                inputEmail.text.toString(),
                inputNewPassword.text.toString(),
                inputConfirmPassword.text.toString(),
                inputPin.text.toString()
            )
        )
    }

    private fun backToLoginFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, LoginFragment())?.commit()
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.updatePasswordFields?.let {
                it.email?.let { inputEmail.setText(it) }
                it.newPassword?.let { inputNewPassword.setText(it) }
                it.confirmPassword?.let { inputConfirmPassword.setText(it) }
                it.pin?.let { inputPin.setText(it) }
            }
        })
    }

    private fun backToForgotPasswordFragment() {
        (activity as AuthActivity).getBack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveUpdatePasswordFields()
    }

    private fun saveUpdatePasswordFields() {
        viewModel.setUpdatePasswordFields(
            UpdatePasswordFields(
                inputNewPassword.text.toString(),
                inputConfirmPassword.text.toString(),
                inputEmail.text.toString(),
                inputPin.text.toString()
            )
        )
    }
}
