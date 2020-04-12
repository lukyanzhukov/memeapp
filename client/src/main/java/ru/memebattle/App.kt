package ru.memebattle

import android.app.Application
import android.content.Context
import client.common.data.*
import client.common.feature.auth.AuthViewModel
import client.common.feature.rating.RatingViewModel
import client.common.feature.settings.SettingsViewModel
import client.common.feature.splash.SplashViewModel
import com.russhwolf.settings.AndroidSettings
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.memebattle.feature.AuthFragment
import ru.memebattle.feature.SettingsFragment
import ru.memebattle.feature.SplashFragment
import ru.memebattle.feature.rating.RatingFragment

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            modules(
                listOf(
                    sharedPreferencesModule,
                    networkModule,
                    splashModule,
                    authModule,
                    ratingModule,
                    settingsModule
                )
            )
        }
    }
}

val networkModule = module {

    single<TokenSource> {
        SettingsTokenSource(AndroidSettings(get()))
    }

    single<LoginSource> {
        SettingsLoginSource(AndroidSettings(get()))
    }

    single {
        MemeClient(get())
    }
}

val splashModule = module {
    scope(named<SplashFragment>()) {
        scoped {
            SplashViewModel(get())
        }
    }
}

val authModule = module {
    scope(named<AuthFragment>()) {
        viewModel {
            AuthViewModel(get(), get(), get())
        }
    }
}

val ratingModule = module {
    scope(named<RatingFragment>()) {
        viewModel {
            RatingViewModel(get()).also(RatingViewModel::getRating)
        }
    }
}

val settingsModule = module {
    scope(named<SettingsFragment>()) {
        scoped {
            SettingsViewModel(get(), get())
        }
    }
}

val sharedPreferencesModule = module {
    single { androidContext().getSharedPreferences("settings", Context.MODE_PRIVATE) }
}

const val PREFS_TOKEN = "token"