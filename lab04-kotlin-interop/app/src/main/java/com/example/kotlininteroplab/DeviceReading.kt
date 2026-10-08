package com.example.kotlininteroplab

data class DeviceReading(
    val name: String?,
    val batteryPercent: Int?,
    val category: String
)

fun demonstrateDataClass(): String {
    val original = DeviceReading("Sensor-A", 15, "Sensor")
    val calibrated = original.copy(batteryPercent = 25)
    val (name, battery, category) = calibrated
    return "$name | $battery% | $category | equal=${original == calibrated}"
}
