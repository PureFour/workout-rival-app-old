package com.ruczajsoftware.workoutrival.data.repository.splashScreen

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import com.ruczajsoftware.workoutrival.data.model.ServerStatus
import com.ruczajsoftware.workoutrival.data.network.ServicesApi
import com.ruczajsoftware.workoutrival.data.network.util.ApiSuccessResponse
import com.ruczajsoftware.workoutrival.data.network.util.GenericApiResponse
import com.ruczajsoftware.workoutrival.data.repository.JobManager
import com.ruczajsoftware.workoutrival.data.repository.NetworkBoundResource
import com.ruczajsoftware.workoutrival.ui.splashScreen.state.SplashScreenViewState
import com.ruczajsoftware.workoutrival.util.AbsentLiveData
import com.ruczajsoftware.workoutrival.util.DataState
import kotlinx.coroutines.Job


class SplashScreenRepositoryImpl(
    private val servicesApi: ServicesApi,
    private val context: Context
) : JobManager("ServerStatusRepository"),
    SplashScreenRepository {

    private val TAG: String = "AppDebug"


    override fun isConnectedToTheInternet(): Boolean {
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            return cm.activeNetworkInfo.isConnected
        } catch (e: Exception) {
            Log.e(TAG, "isConnectedToTheInternet: ${e.message}")
        }
        return false
    }

    override fun checkServerAvailability(): LiveData<DataState<SplashScreenViewState>> {
        return object : NetworkBoundResource<ServerStatus, Any, SplashScreenViewState>(
            isConnectedToTheInternet(),
            true,
            true,
            false
        ) {

            // Ignore
            override fun loadFromCache(): LiveData<SplashScreenViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {}
            override suspend fun createCacheRequestAndReturn() {}

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