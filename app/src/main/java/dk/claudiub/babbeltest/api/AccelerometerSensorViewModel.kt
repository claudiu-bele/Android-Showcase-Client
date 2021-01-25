package dk.claudiub.babbeltest.api

import android.hardware.SensorManager

data class AccelerometerData(
    val x: Float,
    val y: Float,
    val z: Float
)

data class AccelerometerCfg(
    val sensorDelay: Int = SensorManager.SENSOR_DELAY_NORMAL,
    val maxItemsInStack: Int = 20
)

abstract class AccelerometerSensorViewModel(override var config: AccelerometerCfg)
    : AbstractSensorViewModel<AccelerometerData, AccelerometerCfg>()