package com.ruczajsoftware.workoutrival.ui.auth

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import com.ruczajsoftware.workoutrival.R
import com.ruczajsoftware.workoutrival.internal.viewModel
import com.ruczajsoftware.workoutrival.ui.auth.state.AuthStateEvent
import com.ruczajsoftware.workoutrival.ui.dashboard.DashboardActivity
import com.ruczajsoftware.workoutrival.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import com.ruczajsoftware.workoutrival.util.areYouSureDialog
import com.ruczajsoftware.workoutrival.util.base.BaseActivity
import kotlinx.android.synthetic.main.auth_layout.*
import org.kodein.di.KodeinAware
import kotlin.system.exitProcess


class AuthActivity : BaseActivity(), KodeinAware {

    override val contentViewLayout: Int = R.layout.auth_layout
    private val viewModel: AuthViewModel by viewModel()


    override fun afterViews() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                LoginFragment()
            ).addToBackStack("Login").commit()
        subscribeObservers()
    }

    override fun onBackPressed() {
        areYouSureDialog(getString(R.string.do_you_want_exit),
            DialogInterface.OnClickListener { _, _ -> onExitAction() })
    }

    override fun onResume() {
        super.onResume()
        checkPreviousAuthUser()
    }

    private fun checkPreviousAuthUser() {
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAuthEvent)
    }

    private fun onExitAction() {
        finishAffinity()
        exitProcess(0)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let {
                            viewModel.setAuthToken(it)
                        }
                    }
                }

                data.data?.let { event ->
                    event.peekContent().let {
                        it.isRequestPinSuccess?.let {
                            if (it) {
                                navHavePinFragment()
                            }
                        }
                    }
                }


                data.data?.let { event ->
                    event.peekContent().let {
                        it.isUpdatePasswordSuccess?.let {
                            if (it) {
                                viewModel.setStateEvent(
                                    AuthStateEvent.LoginAttemptEvent(
                                        viewModel.getCurrentViewStateOrNew().updatePasswordFields?.email!!,
                                        viewModel.getCurrentViewStateOrNew().updatePasswordFields?.newPassword!!
                                    )
                                )
                            }
                        }
                    }
                }

                data.response?.let { event ->
                    event.peekContent().let { response ->
                        response.message?.let { message ->
                            if (message.equals(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE)) {
                                onFinishCheckPreviousAuthUser()
                            }
                        }
                    }
                }
            }
        })
        viewModel.viewState.observe(this, Observer {
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthViewState: ${it}")
            it.authToken?.let {
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, Observer { dataState ->
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthDataState: ${dataState}")
            dataState.let { authToken ->
                if (authToken != null) {
                    navMainActivity()
                }
            }
        })
    }

    private fun onFinishCheckPreviousAuthUser() {
        fragment_container.visibility = VISIBLE
        logo.visibility = VISIBLE
    }

    private fun navMainActivity() {
        Log.d(TAG, "navMainActivity: called.")
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun navHavePinFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HavePinFragment()).addToBackStack("HavePinFragment")
            .commit()
    }

    override fun displayProgressBar(isVisible: Boolean) {
        if (isVisible) {
            progress_bar.visibility = VISIBLE
        } else {
            progress_bar.visibility = GONE
        }
    }

    fun getBack() {
        super.onBackPressed()
    }
}