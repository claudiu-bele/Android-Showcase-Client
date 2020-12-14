package dk.claudiub.babbeltest

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.claudiub.babbeltest.app.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(Modules.modules)
        }

    }
}