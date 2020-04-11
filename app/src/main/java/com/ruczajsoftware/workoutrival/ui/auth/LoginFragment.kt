package com.ruczajsoftware.workoutrival.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ruczajsoftware.workoutrival.R
import com.ruczajsoftware.workoutrival.internal.viewModel
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthStateEvent.LoginAttemptEvent
import com.ruczajsoftware.workoutrival.ui.auth.state.LoginFields
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


class LoginFragment() : Fragment(R.layout.fragment_login), KodeinAware {

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
        loginButton.setOnClickListener { login() }
        registerButton.setOnClickListener { register() }
        forgotPassword.setOnClickListener { forgotPassword() }
    }

    private fun register() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.fragment_container,
                RegisterFragment()
            )?.addToBackStack("Register")?.commit()
    }

    private fun forgotPassword() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.fragment_container,
                ForgotPasswordFragment()
            )?.addToBackStack("ForgotPassword")?.commit()
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.loginFields?.let {
                it.loginEmail?.let { inputLogin.setText(it) }
                it.loginPassword?.let { inputPassword.setText(it) }
            }
        })
    }

    fun login() {
        viewModel.setStateEvent(
            LoginAttemptEvent(
                inputLogin.text.toString(),
                inputPassword.text.toString()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(
            LoginFields(
                inputLogin.text.toString(),
                inputPassword.text.toString()
            )
        )
    }
}
