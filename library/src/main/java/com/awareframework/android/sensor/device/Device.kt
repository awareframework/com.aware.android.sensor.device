package com.awareframework.android.sensor.device

import android.content.Context
import com.awareframework.android.core.db.Engine
import com.awareframework.android.core.model.SensorConfig

class Device private constructor(
        private var context: Context,
        var config: DeviceConfig = DeviceConfig()
) {

    class DeviceConfig : SensorConfig(dbName = "aware_device.db")

//    var board: String = "",
//    var brand: String = "",
//    var device: String = "",
//    var buildId: String = "",
//    var hardware: String = "",
//    var manufacturer: String = "",
//    var model: String = "",
//    var product: String = "",
//    var serial: String = "",
//    var release: String = "",
//    var releaseType: String = "",
//    var sdk: Int = 0

    class Builder(private val context: Context) {
        private val config: DeviceConfig = DeviceConfig()
        fun setDeviceId(deviceId: String) = apply { config.deviceId = deviceId }
        fun setLabel(label: String) = apply { config.label = label }
        fun setDebug(debug: Boolean) = apply { config.debug = debug }
        fun setDbKey(key: String) = apply { config.dbKey = key }
        fun setDbType(type: Engine.DatabaseType) = apply { config.dbType = type }
        fun build(): Device = Device(context, config)
    }

    fun getDeviceInfo() {

    }
}