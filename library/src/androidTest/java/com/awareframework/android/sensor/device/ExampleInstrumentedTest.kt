package com.awareframework.android.sensor.device

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.awareframework.android.core.db.Engine
import com.awareframework.android.sensor.device.model.DeviceData
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        DeviceSensor.start(appContext, DeviceSensor.Config().apply {
            sensorObserver = object : DeviceSensor.Observer {
                override fun onDeviceChanged(data: DeviceData) {
                    Log.d(DeviceSensor.TAG, data.toString())
                }

            }
            dbType = Engine.DatabaseType.ROOM
            debug = true
            //more configuration...
        })
    }
}
