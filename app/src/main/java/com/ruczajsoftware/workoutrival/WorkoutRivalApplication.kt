package com.ruczajsoftware.workoutrival

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.ruczajsoftware.workoutrival.data.db.AppDatabase
import com.ruczajsoftware.workoutrival.data.network.networkController.NetworkController
import com.ruczajsoftware.workoutrival.data.repository.auth.AuthRepository
import com.ruczajsoftware.workoutrival.data.repository.auth.AuthRepositoryImpl
import com.ruczajsoftware.workoutrival.data.repository.splashScreen.SplashScreenRepository
import com.ruczajsoftware.workoutrival.data.repository.splashScreen.SplashScreenRepositoryImpl
import com.ruczajsoftware.workoutrival.session.SessionManager
import com.ruczajsoftware.workoutrival.util.viewmodel.ViewModelFactory
import com.ruczajsoftware.workoutrival.util.viewmodel.viewModelModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class WorkoutRivalApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein {
        import(androidXModule(this@WorkoutRivalApplication))
        import(viewModelModule)
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { SessionManager(instance()) }

        bind() from singleton { instance<AppDatabase>().getAuthTokenDao() }
        bind() from singleton { NetworkController() }
        bind<SplashScreenRepository>() with singleton {
            SplashScreenRepositoryImpl(
                instance(),
                instance()
            )
        }
        bind<AuthRepository>() with singleton {
            AuthRepositoryImpl(
                instance(),
                instance(),
                instance()
            )
        }
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
    }
}