package dk.claudiub.babbeltest.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.claudiub.babbeltest.app.TranslatedItem
import dk.claudiub.babbeltest.core.AsyncResource

/** Model for holding when a specific sensor reading has occured, used in [AbstractSensorViewModel.getDataHistory]
 *
 */
data class SensorHistoricReading<T : Any>(
    val time: Long,
    val data: T
)

/** Abstract version of a Sensor view model. Uses generic [T] for the data type that will be transmitted
 * so we can create multiple different Sensor View Models and their implementations
 *
 * When creating actual implementations of this viewmodel's subclasses we can provide through the DI system
 * access to any capability we are looking for (accelerometer, gps, gyroscope, heartbeat, etc)
 *
 */
abstract class AbstractSensorViewModel<T : Any, Config : Any> : ViewModel() {

    abstract var config: Config;

    // region data
    /** When app is started
     */
    abstract fun isStarted(): LiveData<Boolean>

    /** Also restarts
     */
    abstract fun start()

    /** Gets the last reading
     */
    abstract fun getLastEntry(): LiveData<T>

    /** Gets the history of last readings.
     *
     */
    abstract fun getDataHistory(): LiveData<List<SensorHistoricReading<T>>>
    // endregion


    // endregion

}