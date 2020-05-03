package ru.memebattle

import android.app.Application
import android.content.Context
import client.common.data.*
import client.common.feature.auth.AuthViewModel
import client.common.feature.game.GameViewModel
import client.common.feature.memebattle.MemeBattleViewModel
import client.common.feature.memechill.MemeChillViewModel
import client.common.feature.rating.RatingViewModel
import client.common.feature.settings.SettingsViewModel
import client.common.feature.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.memebattle.feature.MemeChillFragment

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            modules(
                listOf(
                    sharedPreferencesModule,
                    networkModule,
                    authModule,
                    ratingModule,
                    gameModule,
                    settingsModule,
                    memeBattleModule,
                    memeChillModule
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

val authModule = module {
    viewModel {
        AuthViewModel(get(), get(), get())
    }
}

val ratingModule = module {
    viewModel {
        RatingViewModel(get()).also(RatingViewModel::getRating)
    }
}

val gameModule = module {
    single<GameModeSource> {
        SettingsGameModeSource(AndroidSettings(get()))
    }
    viewModel {
        GameViewModel(get())
    }
}

val memeBattleModule = module {
    viewModel {
        MemeBattleViewModel(get(), get()).also(MemeBattleViewModel::connect)
    }
}

val memeChillModule = module {
    viewModel {
        MemeChillViewModel(get())
    }
}

val settingsModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }
}

val sharedPreferencesModule = module {
    single { androidContext().getSharedPreferences("settings", Context.MODE_PRIVATE) }
}