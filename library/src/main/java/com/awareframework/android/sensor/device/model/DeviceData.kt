package com.awareframework.android.sensor.device.model

import com.awareframework.android.core.model.AwareObject

/**
 * Contains Device data
 */
data class DeviceData(
        var board: String? = null,
        var brand: String? = null,
        var device: String? = null,
        var buildId: String? = null,
        var hardware: String? = null,
        var manufacturer: String? = null,
        var model: String? = null,
        var product: String? = null,
        var serial: String? = null,
        var release: String? = null,
        var releaseType: String? = null,
        var sdk: String? = null
) : AwareObject(jsonVersion = 1) {
    companion object {
        const val TABLE_NAME = "deviceData"
    }

    override fun toString(): String = toJson()
}