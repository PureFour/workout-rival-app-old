package com.ruczajsoftware.workoutrival.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ruczajsoftware.workoutrival.R

import com.ruczajsoftware.workoutrival.internal.viewModel
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthStateEvent
import com.ruczajsoftware.workoutrival.ui.auth.state.RegistrationFields
import kotlinx.android.synthetic.main.fragment_register.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


class RegisterFragment : Fragment(R.layout.fragment_register), KodeinAware {

    override val kodein by closestKodein()
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.cancelActiveJobs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

        subscribeObservers()
    }

    private fun setupListeners() {
        registerButton.setOnClickListener { register() }
        loginButton.setOnClickListener { backToLogin() }

    }

    private fun backToLogin() {
        (activity as AuthActivity).getBack()
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.registrationFields?.let {
                it.registrationEmail?.let { inputEmail.setText(it) }
                it.registrationUsername?.let { inputLogin.setText(it) }
                it.registrationPassword?.let { inputPassword.setText(it) }
                it.registrationConfirmPassword?.let { inputConfirmPassword.setText(it) }
            }
        })
    }

    fun register() {
        viewModel.setStateEvent(
            AuthStateEvent.RegisterAttemptEvent(
                inputEmail.text.toString(),
                inputLogin.text.toString(),
                inputPassword.text.toString(),
                inputConfirmPassword.text.toString()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                inputEmail.text.toString(),
                inputLogin.text.toString(),
                inputPassword.text.toString(),
                inputConfirmPassword.text.toString()
            )
        )
    }
}