package com.awareframework.android.sensor.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.awareframework.android.core.AwareSensor
import com.awareframework.android.core.model.SensorConfig
import com.awareframework.android.sensor.device.model.DeviceData

class DeviceSensor : AwareSensor() {

    interface Observer {
        fun onDeviceChanged(data: DeviceData)
    }

    data class Config(var sensorObserver: Observer? = null) : SensorConfig(dbPath = "aware_device") {
        override fun <T : SensorConfig> replaceWith(config: T) {
            super.replaceWith(config)

            if (config is Config) {
                sensorObserver = config.sensorObserver
            }
        }
    }

    companion object {
        const val TAG = "AWARE::Device"
        const val ACTION_AWARE_DEVICE = "ACTION_AWARE_DEVICE"
        const val ACTION_AWARE_DEVICE_START = "com.awareframework.android.sensor.device.SENSOR_START"
        const val ACTION_AWARE_DEVICE_STOP = "com.awareframework.android.sensor.device.SENSOR_STOP"
        const val ACTION_AWARE_DEVICE_SET_LABEL = "com.awareframework.android.sensor.device.SET_LABEL"
        const val EXTRA_LABEL = "label"
        const val ACTION_AWARE_DEVICE_SYNC = "com.awareframework.android.sensor.device.SENSOR_SYNC"

        val CONFIG = Config()

        fun start(context: Context, config: Config? = null) {
            if (config != null) CONFIG.replaceWith(config)
            context.startService(Intent(context, DeviceSensor::class.java))
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, DeviceSensor::class.java))
        }
    }

    private val deviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return

            when (intent.action) {
                ACTION_AWARE_DEVICE_SET_LABEL -> {
                    intent.getStringExtra(EXTRA_LABEL)?.let { CONFIG.label = it }
                }
                ACTION_AWARE_DEVICE_SYNC -> onSync(intent)
            }
        }

    }

    override fun onCreate() {
        super.onCreate()

        initializeDbEngine(CONFIG)

        registerReceiver(deviceReceiver, IntentFilter().apply {
            addAction(ACTION_AWARE_DEVICE_SET_LABEL)
            addAction(ACTION_AWARE_DEVICE_SYNC)
        })

        logd("Device service created!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val data = DeviceData().apply {
            timestamp = System.currentTimeMillis()
            deviceId = CONFIG.deviceId
            board = Build.BOARD
            brand = Build.BRAND
            device = Build.DEVICE
            buildId = Build.DISPLAY
            hardware = Build.HARDWARE
            manufacturer = Build.MANUFACTURER
            model = Build.MODEL
            product = Build.PRODUCT
            serial = Build.SERIAL
            release = Build.VERSION.RELEASE
            releaseType = Build.TYPE
            sdk = Build.VERSION.SDK_INT
            label = CONFIG.label
        }

        dbEngine?.save(data, DeviceData.TABLE_NAME)
        CONFIG.sensorObserver?.onDeviceChanged(data)

        sendBroadcast(Intent(ACTION_AWARE_DEVICE))

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        dbEngine?.close()
        unregisterReceiver(deviceReceiver)
        logd("$TAG terminated.")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSync(intent: Intent?) {
        dbEngine?.startSync(DeviceData.TABLE_NAME)
    }

    class DeviceSensorBroadcastReceiver : AwareSensor.SensorBroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context ?: return

            logd("$TAG broadcast received. action " + intent?.action)

            when (intent?.action) {
                SENSOR_START_ENABLED -> {
                    logd("$TAG enabled: " + CONFIG.enabled)

                    if (CONFIG.enabled) start(context)
                }

                ACTION_AWARE_DEVICE_STOP, SENSOR_STOP_ALL -> {
                    logd("Stopping $TAG.")
                    stop(context)
                }

                ACTION_AWARE_DEVICE_START -> {
                    logd("Starting $TAG.")
                    start(context)
                }
            }
        }
    }

    override fun sendBroadcast(intent: Intent?) {
        intent?.let { logd(it.action) }
        super.sendBroadcast(intent)
    }
}

private fun logd(text: String) {
    if (DeviceSensor.CONFIG.debug) Log.d(DeviceSensor.TAG, text)
}