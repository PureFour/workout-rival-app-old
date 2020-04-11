package com.ruczajsoftware.workoutrival.data.repository.splashScreen

import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.ui.splashScreen.state.SplashScreenViewState
import com.ruczajsoftware.workoutrival.util.DataState

interface SplashScreenRepository {
    fun checkServerAvailability(): LiveData<DataState<SplashScreenViewState>>
    fun cancelActiveJobsOver()
}
