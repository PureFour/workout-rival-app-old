package com.ruczajsoftware.workoutrival.ui.splashScreen

import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.data.repository.splashScreen.SplashScreenRepository
import com.ruczajsoftware.workoutrival.data.servicesModel.ServerStatus
import com.ruczajsoftware.workoutrival.ui.splashScreen.state.SplashScreenStateEvent
import com.ruczajsoftware.workoutrival.ui.splashScreen.state.SplashScreenViewState
import com.ruczajsoftware.workoutrival.util.DataState
import com.ruczajsoftware.workoutrival.util.viewmodel.BaseViewModel

class SplashScreenViewModel(private val splashScreenRepository: SplashScreenRepository) :
    BaseViewModel<SplashScreenStateEvent, SplashScreenViewState>() {


    override fun handleStateEvent(stateEvent: SplashScreenStateEvent): LiveData<DataState<SplashScreenViewState>> {
        when (stateEvent) {
            is SplashScreenStateEvent.CheckStatus -> {
                return splashScreenRepository.checkServerAvailability()
            }
        }
    }

    override fun initNewViewState(): SplashScreenViewState {
        return SplashScreenViewState()
    }

    fun setServerStatus(serverStatus: ServerStatus) {
        val update = getCurrentViewStateOrNew()
        update.serverStatus = serverStatus
        setViewState(update)
    }
}