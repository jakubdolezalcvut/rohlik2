package cz.rohlik

import android.app.Application
import cz.rohlik.di.appModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

public class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            modules(appModule)
        }
    }
}
