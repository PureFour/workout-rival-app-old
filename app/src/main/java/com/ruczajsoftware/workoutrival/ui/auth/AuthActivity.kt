package com.ruczajsoftware.workoutrival.ui.auth

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.ruczajsoftware.workoutrival.R
import com.ruczajsoftware.workoutrival.internal.viewModelActivity
import com.ruczajsoftware.workoutrival.ui.dashboard.DashboardActivity
import com.ruczajsoftware.workoutrival.util.areYouSureDialog
import com.ruczajsoftware.workoutrival.util.base.BaseActivity
import kotlinx.android.synthetic.main.auth_layout.*
import org.kodein.di.KodeinAware
import kotlin.system.exitProcess


class AuthActivity : BaseActivity(), KodeinAware {

    override val contentViewLayout: Int = R.layout.auth_layout

    private val viewModel: AuthViewModel by viewModelActivity()

    override fun displayProgressBar(bool: Boolean) {
        if (bool) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }


    override fun afterViews() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                LoginFragment()
            ).commit()
        subscribeObservers()
    }

    override fun onBackPressed() {
        areYouSureDialog("Do you want exit?",
            DialogInterface.OnClickListener { _, _ ->
                finishAffinity()
                exitProcess(0)
            })
    }


    private fun subscribeObservers() {
        viewModel.dataState.observe(this, androidx.lifecycle.Observer { dataState ->
            onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let {
                            viewModel.setAuthToken(it)
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
                if (authToken) {
                    navMainActivity()
                }
            }
        })
    }

    fun navMainActivity() {
        Log.d(TAG, "navMainActivity: called.")
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

}