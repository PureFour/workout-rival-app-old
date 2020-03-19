package com.ruczajsoftware.workoutrival.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ruczajsoftware.workoutrival.R
import com.ruczajsoftware.workoutrival.internal.viewModelFragment
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthStateEvent.LoginAttemptEvent
import com.ruczajsoftware.workoutrival.ui.auth.state.LoginFields
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment() : Fragment(R.layout.fragment_login), KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: AuthViewModel by viewModelFragment()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        setupListeners()

    }

    private fun setupListeners() {
        loginButton.setOnClickListener { login() }
        registerButton.setOnClickListener { register() }
    }

    private fun register() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.fragment_container,
                RegisterFragment()
            )?.commit()
        subscribeObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.cancelActiveJobs()
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.loginFields?.let {
                it.login_email?.let { inputLogin.setText(it) }
                it.login_password?.let { inputPassword.setText(it) }
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
