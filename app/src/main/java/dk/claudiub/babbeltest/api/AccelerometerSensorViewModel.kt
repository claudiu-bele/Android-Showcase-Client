package dk.claudiub.babbeltest.api

data class AccelerometerData(
    val x: Float,
    val y: Float,
    val z: Float
)

abstract class AccelerometerSensorViewModel : AbstractSensorViewModel<AccelerometerData>()