package com.ruczajsoftware.workoutrival.ui.splashScreen.state

import android.os.Parcelable
import com.ruczajsoftware.workoutrival.data.model.ServerStatus
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class SplashScreenViewState(
    var serverStatus: @RawValue ServerStatus? = null
) : Parcelable