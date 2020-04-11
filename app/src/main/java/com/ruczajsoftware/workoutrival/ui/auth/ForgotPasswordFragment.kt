package com.ruczajsoftware.workoutrival.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ruczajsoftware.workoutrival.R
import com.ruczajsoftware.workoutrival.internal.viewModel
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthStateEvent
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password), KodeinAware {

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
        requestNewPinButton.setOnClickListener { requestNewPin() }
        havePinButton.setOnClickListener { goToHavePinFragment() }
    }

    private fun backToLoginFragment() {
        (activity as AuthActivity).getBack()
    }

    private fun goToHavePinFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, HavePinFragment())?.addToBackStack("HavePin")
            ?.commit()
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.forgotPasswordEmail?.let {
                inputEmail.setText(it)
            }
        })
    }

    private fun requestNewPin() {
        viewModel.setStateEvent(AuthStateEvent.RequestNewPinEvent(inputEmail.text.toString()))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setEmailOnForgotPasswordFragment(inputEmail.text.toString())
    }
}