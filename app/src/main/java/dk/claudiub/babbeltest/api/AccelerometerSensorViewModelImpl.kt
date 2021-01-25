package dk.claudiub.babbeltest.api

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import kotlin.collections.ArrayList

class AccelerometerSensorViewModelImpl(val context: Context, cfg: AccelerometerCfg)
    : AccelerometerSensorViewModel(cfg), SensorEventListener {

    private var accelerometerHistory: ArrayList<SensorHistoricReading<AccelerometerData>> = ArrayList()
    private var lastItem : AccelerometerData? = null;
    private var accelerometer: Sensor? = null

    private var lastItemLiveData: MutableLiveData<AccelerometerData>
        = MutableLiveData(lastItem)
    private var startedLiveData = MutableLiveData<Boolean>(false)
    private var accelerometerHistoryLiveData: MutableLiveData<List<SensorHistoricReading<AccelerometerData>>>
        = MutableLiveData(accelerometerHistory)

    init {
        start()
    }

    override fun isStarted(): LiveData<Boolean> = startedLiveData

    override fun start() {
        if(this.startedLiveData.value != true) {
            this.startedLiveData.postValue(true)
            initAccelerometer()
        }
    }

    override fun getLastEntry(): LiveData<AccelerometerData> = lastItemLiveData
    override fun getDataHistory(): LiveData<List<SensorHistoricReading<AccelerometerData>>> = accelerometerHistoryLiveData

    private fun initAccelerometer(){

        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, config.sensorDelay);
        } else {
            // would post to an error live data
        }
    }

    // region SensorEventListener
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val time = Calendar.getInstance().timeInMillis
            val accData = AccelerometerData(it.values[0], it.values[1], it.values[2]);
            val historicReading = SensorHistoricReading(time, accData)

            lastItem = accData;
            lastItemLiveData.postValue(accData);

            accelerometerHistory.add(0, historicReading)
            if(accelerometerHistory.size > config.maxItemsInStack) {
                accelerometerHistory.removeAt(accelerometerHistory.lastIndex)
            }
            accelerometerHistoryLiveData.postValue(accelerometerHistory)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // expand based on Config later
    }
    // endregion
}