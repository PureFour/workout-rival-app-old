package com.ruczajsoftware.workoutrival.util.viewmodel

import com.ruczajsoftware.workoutrival.internal.bindViewModel
import com.ruczajsoftware.workoutrival.ui.auth.AuthViewModel
import com.ruczajsoftware.workoutrival.ui.splashScreen.SplashScreenViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

//In this file we are binding all of viewModels

val viewModelModule = Kodein.Module(name = "viewModelModule") {
    bindViewModel<SplashScreenViewModel>() with singleton { SplashScreenViewModel(instance()) }
    bindViewModel<AuthViewModel>() with singleton { AuthViewModel(instance()) }
}