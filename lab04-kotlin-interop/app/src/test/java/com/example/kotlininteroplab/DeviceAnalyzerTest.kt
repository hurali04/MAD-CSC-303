package com.example.kotlininteroplab

import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceAnalyzerTest {

    // T2: Convert DeviceReading and rebuild (Java repository constructing Kotlin data class instances)
    @Test
    fun testT2_convertDeviceReadingAndRebuild() {
        println("=== TEST T2: Convert DeviceReading and Rebuild ===")
        val devices = LegacyDeviceRepository.loadDevices()
        println("Constructed ${devices.size} DeviceReading instances from LegacyDeviceRepository.java:")
        devices.forEachIndexed { idx, dev ->
            println("  [$idx] Name: ${dev.name}, Battery: ${dev.batteryPercent}, Category: ${dev.category}")
        }
        assertEquals(5, devices.size)
    }

    // T4: Battery null, -1, 0, 100, and 140
    @Test
    fun testT4_batteryNullMinus1_0_100And140() {
        println("\n=== TEST T4: Battery Null, -1, 0, 100, and 140 ===")
        val inputs = listOf<Int?>(null, -1, 0, 100, 140)
        val expected = listOf("unknown", "invalid (-1)", "0%", "100%", "invalid (140)")

        inputs.zip(expected).forEach { (input, exp) ->
            val actual = DeviceAnalyzer.formatBattery(input)
            println("  Input: ${input?.toString() ?: "null"} -> Result: \"$actual\"")
            assertEquals(exp, actual)
        }
    }

    // T6: Press Analyze devices (Generating the full report)
    @Test
    fun testT6_pressAnalyzeDevices() {
        println("\n=== TEST T6: Press Analyze Devices (Full Report) ===")
        val report = DeviceAnalyzer.loadAndBuildReport()
        println(report)
    }

    // T8: Run unit tests (Core logic & normalization tests)
    @Test
    fun testT8_runUnitTests() {
        println("\n=== TEST T8: Run Unit Tests (Summarize & Normalization) ===")
        assertEquals("Unnamed device", DeviceAnalyzer.normalizeName(null))
        assertEquals("Unnamed device", DeviceAnalyzer.normalizeName("   "))
        assertEquals("Sensor-A", DeviceAnalyzer.normalizeName(" Sensor-A "))
        println("  normalizeName checks: PASSED")

        val testDevices = listOf(
            DeviceReading("A", 10, "Sensor"),
            DeviceReading("B", 80, "Gateway"),
            DeviceReading(null, null, "Camera"),
            DeviceReading("C", 120, "Tracker")
        )
        val summary = DeviceAnalyzer.summarize(testDevices)
        println("  Summarize output: total=${summary.total}, valid=${summary.validBatteryCount}, low=${summary.lowBatteryCount}, invalid/missing=${summary.invalidOrMissingCount}, avg=${summary.averageBattery}%")
        assertEquals(4, summary.total)
        assertEquals(2, summary.validBatteryCount)
        assertEquals(1, summary.lowBatteryCount)
        assertEquals(2, summary.invalidOrMissingCount)
        assertEquals(45.0, summary.averageBattery, 0.001)
        println("  summarize checks: PASSED")
    }
}
