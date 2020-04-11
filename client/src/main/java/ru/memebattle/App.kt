package ru.memebattle

import android.app.Application
import android.content.Context
import client.common.data.MemeClient
import client.common.data.SettingsTokenSource
import client.common.data.TokenSource
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            modules(
                listOf(
                    sharedPreferencesModule,
                    networkModule
                )
            )
        }
    }
}

val networkModule = module {

    single<TokenSource> {
        SettingsTokenSource(AndroidSettings(get()))
    }

    single<HttpClient> {
        MemeClient(get())
    }
}

val sharedPreferencesModule = module {
    single { androidContext().getSharedPreferences("settings", Context.MODE_PRIVATE) }
}

const val PREFS_TOKEN = "token"
const val PREFS_EMAIL = "email"