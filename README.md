# AWARE Screen

[![jitpack-badge](https://jitpack.io/v/awareframework/com.aware.android.sensor.device.svg)](https://jitpack.io/#awareframework/com.aware.android.sensor.device)

The device sensor monitors the device manufacturer, model, operating system version and other information.

## Public functions

### DeviceSensor

+ `start(context: Context, config: DeviceSensor.Config?)`: Starts the sensor with the optional configuration.
+ `stop(context: Context)`: Stops the service.

### DeviceSensor.Config

Class to hold the configuration of the sensor.

#### Fields

+ `sensorObserver: ScreenSensor.Observer`: Callback for live data updates.
+ `enabled: Boolean` Sensor is enabled or not. (default = `false`)
+ `debug: Boolean` enable/disable logging to `Logcat`. (default = `false`)
+ `label: String` Label for the data. (default = "")
+ `deviceId: String` Id of the device that will be associated with the events and the sensor. (default = "")
+ `dbEncryptionKey` Encryption key for the database. (default = `null`)
+ `dbType: Engine` Which db engine to use for saving data. (default = `Engine.DatabaseType.ROOM`)
+ `dbPath: String` Path of the database. (default = "aware_screen")
+ `dbHost: String` Host for syncing the database. (default = `null`)

## Broadcasts

+ `DeviceSensor.ACTION_AWARE_DEVICE` fired when device is profiled.

## Data Representations

### Device Data
| Field        | Type   | Description                                                            |
| ------------ | ------ | ---------------------------------------------------------------------- |
| model | String | Device's model |
| serial | String | Device's serial number. Newer devices no longer report this. |
| releaseType | String | Android's type of release (user, userdebug, eng) |
| sdk   | string | Android's SDK level
| release | String | Android's release name |
| product   | String    | Device's product name |
| manufacturer | String | Device's manufacturer name |
| hardware  |   String  | Hardware codename |
| buildId   |   String  | Android OS build ID   |
| device    |   String  | Manufacturers' devide name    |
| brand        | String | Manufacturers' brand name                                              |
| board        | String | Manufacturers' board name                                       |
| deviceId     | String | AWARE device UUID                                                      |
| label        | String | Customizable label. Useful for data calibration or traceability        |
| timestamp    | Long   | unixtime milliseconds since 1970                                       |
| timezone     | Int    | [Raw timezone offset][1] of the device                                 |
| os           | String | Operating system of the device (ex. android)                           |

## Example usage

```kotlin
// To start the service.
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

// To stop the service
DeviceSensor.stop(appContext)
```

## License

Copyright (c) 2018 AWARE Mobile Context Instrumentation Middleware/Framework (http://www.awareframework.com)

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

[1]: https://developer.android.com/reference/java/util/TimeZone#getRawOffset()
