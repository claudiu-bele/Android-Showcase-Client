package dk.claudiub.babbeltest.app

import android.hardware.SensorManager
import com.google.gson.Gson
import dk.claudiub.babbeltest.api.*
import dk.claudiub.babbeltest.api_impl.GameViewModelImpl
import dk.claudiub.babbeltest.api_impl.TranslationsRepositoryImpl
import dk.claudiub.babbeltest.api_impl.GsonTranslationsUseCase
import dk.claudiub.babbeltest.core.coroutine.DispatcherProvider
import dk.claudiub.babbeltest.core.coroutine.DispatcherProviderImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    val modules = module {
        // val lifecycleCoroutineScope: LifecycleCoroutineScope
        // override val coroutineContext: CoroutineContext
        single<DispatcherProvider> { DispatcherProviderImpl() }
        single { Gson() }

        single<TranslationsUseCase> { GsonTranslationsUseCase(get(), get(), get()) }
        single<TranslationsRepository> { TranslationsRepositoryImpl(get()) }

        viewModel<GameViewModel> { GameViewModelImpl(get(), get()) }

        // sensor stuff
        viewModel<AccelerometerSensorViewModel>
        {
            AccelerometerSensorViewModelImpl(
                // needing context to get system services
                get(),
                // passing accelerometer cfg to further configure sensor
                AccelerometerCfg(sensorDelay = SensorManager.SENSOR_DELAY_FASTEST)
            )
        }
    }
}