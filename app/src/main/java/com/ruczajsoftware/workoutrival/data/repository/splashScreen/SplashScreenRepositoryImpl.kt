package com.ruczajsoftware.workoutrival.data.repository.splashScreen

import android.util.Log
import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.data.network.ServicesApi
import com.ruczajsoftware.workoutrival.data.network.util.ApiSuccessResponse
import com.ruczajsoftware.workoutrival.data.network.util.GenericApiResponse
import com.ruczajsoftware.workoutrival.data.repository.JobManager
import com.ruczajsoftware.workoutrival.data.repository.NetworkBoundResource
import com.ruczajsoftware.workoutrival.data.servicesModel.ServerStatus
import com.ruczajsoftware.workoutrival.session.SessionManager
import com.ruczajsoftware.workoutrival.ui.splashScreen.state.SplashScreenViewState
import com.ruczajsoftware.workoutrival.util.DataState
import kotlinx.coroutines.Job


class SplashScreenRepositoryImpl(
    private val servicesApi: ServicesApi,
    private val sessionManager: SessionManager
) : JobManager("ServerStatusRepository"),
    SplashScreenRepository {

    private val TAG: String = "AppDebug"


    override fun checkServerAvailability(): LiveData<DataState<SplashScreenViewState>> {
        return object : NetworkBoundResource<ServerStatus, Any, SplashScreenViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {


            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ServerStatus>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")
                onCompleteJob(
                    DataState.data(
                        data = SplashScreenViewState(
                            serverStatus = response.body
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<ServerStatus>> {
                return servicesApi.isServerAvailable()
            }

            override fun setJob(job: Job) {
                addJob("serverStatus", job)
            }

        }.asLiveData()
    }

    override fun cancelActiveJobsOver() {
        super.cancelActiveJobs()
    }
}