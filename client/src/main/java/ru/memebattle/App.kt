package ru.memebattle

import android.app.Application
import android.content.Context
import client.common.data.*
import client.common.feature.auth.AuthViewModel
import client.common.feature.game.GameViewModel
import client.common.feature.localization.LastLocaleStore
import client.common.feature.localization.LocalizationDelegate
import client.common.feature.localization.LocalizationViewModel
import client.common.feature.memebattle.MemeBattleViewModel
import client.common.feature.memechill.MemeChillViewModel
import client.common.feature.rating.RatingViewModel
import client.common.feature.settings.SettingsViewModel
import client.common.feature.splash.SplashViewModel
import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.memebattle.feature.fatal.FatalViewModel
import ru.memebattle.feature.firstwin.FirstWinViewModel
import ru.memebattle.feature.gameonboarding.GameOnboardingViewModel
import ru.memebattle.feature.locale.AndroidDeviceLocaleSource
import ru.memebattle.feature.signout.TrySignOutViewModel

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
                    gameModule,
                    settingsModule,
                    memeBattleModule,
                    localizationModule,
                    gameOnboardingModule,
                    memeChillModule,
                    firstWinModule,
                    fatalModule,
                    signOutModule
                )
            )
        }
    }
}

val networkModule = module {

    single<TokenSource> {
        SettingsTokenSource(get())
    }

    single<LoginSource> {
        SettingsLoginSource(get())
    }

    single {
        MemeClient(get())
    }
}

private val splashModule = module {
    single<BaseSettingsSource> {
        BaseSettingsSourceImpl(get())
    }
    viewModel {
        SplashViewModel(get(), get(), AndroidDeviceLocaleSource, get(), get())
    }
}

private val authModule = module {
    viewModel {
        AuthViewModel(get(), get(), get(), get())
    }
}

private val ratingModule = module {
    viewModel {
        RatingViewModel(get())
    }
}

private val gameModule = module {
    single<GameModeSource> {
        SettingsGameModeSource(get())
    }
    viewModel {
        GameViewModel(get())
    }
}

private val memeBattleModule = module {
    viewModel {
        MemeBattleViewModel(
            get(),
            get(),
            AndroidDeviceLocaleSource
        ).also(MemeBattleViewModel::connect)
    }
}

private val memeChillModule = module {
    viewModel {
        MemeChillViewModel(get())
    }
}

private val settingsModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }
}

private val sharedPreferencesModule = module {
    single<Settings> {
        AndroidSettings(
            androidContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        )
    }
}

private val localizationModule = module {
    single {
        LocalizationDb(AndroidSqliteDriver(LocalizationDb.Schema, get(), "localeDb")).localeQueries
    }
    single {
        LocalizationDelegate(get())
    }
    single {
        LastLocaleStore(get())
    }
    viewModel {
        LocalizationViewModel(get())
    }
}

private val gameOnboardingModule = module {
    viewModel {
        GameOnboardingViewModel()
    }
}

private val firstWinModule = module {
    viewModel {
        FirstWinViewModel()
    }
}

private val fatalModule = module {
    viewModel {
        FatalViewModel()
    }
}

private val signOutModule = module {
    viewModel {
        TrySignOutViewModel()
    }
}