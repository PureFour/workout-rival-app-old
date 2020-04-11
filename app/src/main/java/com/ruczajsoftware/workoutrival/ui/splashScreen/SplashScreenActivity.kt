package com.ruczajsoftware.workoutrival.ui.splashScreen

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.ruczajsoftware.workoutrival.R
import com.ruczajsoftware.workoutrival.data.servicesModel.ServerStatus
import com.ruczajsoftware.workoutrival.internal.viewModel
import com.ruczajsoftware.workoutrival.ui.auth.AuthActivity
import com.ruczajsoftware.workoutrival.ui.splashScreen.state.SplashScreenStateEvent.CheckStatus
import com.ruczajsoftware.workoutrival.util.base.BaseActivity
import org.kodein.di.KodeinAware

class SplashScreenActivity() : BaseActivity(), KodeinAware {

    override val contentViewLayout: Int = R.layout.splash_screen_activity

    private val viewModel: SplashScreenViewModel by viewModel()

    companion object {
        private const val SERVER_STATUS = "UP"
    }

    override fun afterViews() {
        viewModel.setStateEvent(CheckStatus())
        hideBottomNavigation()
        subscribeObservers()
    }

    private fun hideBottomNavigation() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }


    private fun subscribeObservers() {
        viewModel.dataState.observe(this, androidx.lifecycle.Observer { dataState ->
            onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.serverStatus?.let {
                            viewModel.setServerStatus(it)
                        }
                    }
                }

            }
        })

        viewModel.viewState.observe(this, Observer {
            Log.d(TAG, "SplashScreenActivity, subscribeObservers: ServerStatusViewSate: ${it}")
            it.serverStatus?.let {
                redirectAuthActivity(it)
            }
        })
    }


    private fun redirectAuthActivity(serverStatus: ServerStatus) {
        if (serverStatus.status == SERVER_STATUS) {
            startActivity(Intent(this, AuthActivity::class.java))
        }
    }

    override fun displayProgressBar(isVisible: Boolean) {}

}
