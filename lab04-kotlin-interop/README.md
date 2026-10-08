# CSC303 Lab 04: Kotlin Fundamentals and Java Interoperability

## Overview
This project is a mixed Java-Kotlin Android application developed for CSC303 - Mobile Application Development. It demonstrates null safety, data classes, collection transformations with lambdas, unit testing, and bi-directional Java-Kotlin interoperability.

## Mixed-Language Architecture & Design
- **Java UI & Repository Layer**:
  - `MainActivity.java`: Java Activity serving as the entry point, invoking Kotlin analyzer functions when the "Analyze devices" button is clicked.
  - `LegacyDeviceRepository.java`: Java repository providing mock device records and alias lookup with `@NonNull` and `@Nullable` annotations.
- **Kotlin Data & Domain Layer**:
  - `DeviceReading.kt`: Kotlin data class representing individual device measurements (`name: String?`, `batteryPercent: Int?`, `category: String`).
  - `DeviceSummary.kt`: Kotlin data class holding summarized statistics (total count, valid battery count, low battery count, invalid/missing count, average battery percentage).
  - `DeviceAnalyzer.kt`: Kotlin singleton object exposing `@JvmStatic` helper and processing functions (`normalizeName`, `formatBattery`, `summarize`, `loadAndBuildReport`).
- **Unit Tests**:
  - `DeviceAnalyzerTest.kt`: JUnit 4 unit tests verifying data class conversion, battery formatting, normalization, summary statistics, and full report generation.

## Data Processing Rules
1. **Name Normalization (`normalizeName`):**
   - Trims whitespace and checks if non-empty using `takeIf`.
   - Fallbacks to `"Unnamed device"` if null or blank via Elvis operator (`?:`).
2. **Battery Formatting (`formatBattery`):**
   - `null` $\rightarrow$ `"unknown"`
   - `0..100` $\rightarrow$ `"$value%"` (valid range)
   - Other values ($<0$ or $>100$) $\rightarrow$ `"invalid ($value)"`
3. **Summary Statistics (`summarize`):**
   - Uses `mapNotNull` with `takeIf { it in 0..100 }` to extract valid battery values (`[15, 85, 40]`).
   - Calculates valid battery average ($140 / 3 = 46.7\%$), low battery count ($<20\%$), and invalid/missing count ($5 - 3 = 2$).

## Diagnosed Issue & Resolution
- **Symptom / Issue:** Java platform type uncertainty (`String!`) when calling `LegacyDeviceRepository.findAlias()`. If `@Nullable` is omitted in Java, Kotlin treats the return type as `String!`, which risks a runtime `NullPointerException` if accessed directly for device names without an alias.
- **Diagnosis & Fix:** Added `androidx.annotation.Nullable` to `findAlias()` in `LegacyDeviceRepository.java`. This forced Kotlin to treat the return value explicitly as `String?`, requiring compile-time safe call chains (`alias?.let { "$safeName ($it)" } ?: safeName`), preventing runtime crashes.

## Verification Matrix (T1–T8)

| ID | Input / Action | Expected Behaviour | Result |
|----|----------------|--------------------|--------|
| T1 | Clean build and launch | Java activity compiles; Java and Kotlin interoperate seamlessly. | PASS |
| T2 | Data class conversion | `DeviceReading.kt` is a data class; Java repository constructs instances cleanly. | PASS |
| T3 | Null or blank device name | `normalizeName` returns `"Unnamed device"` without NPE. | PASS |
| T4 | Battery null, -1, 0, 100, 140 | Missing (`"unknown"`) and invalid (`"invalid (140)"`) values labeled; 0 and 100 stay valid boundaries. | PASS |
| T5 | Repository data analysis | Summary shows Total: 5, Valid: 3, Low: 1, Invalid/Missing: 2, Average: 46.7%. | PASS |
| T6 | Press "Analyze devices" | Multi-line sorted report is displayed with aliases mapped from Java repository. | PASS |
| T7 | Java to Kotlin to Java call path | `MainActivity.java` calls `DeviceAnalyzer.loadAndBuildReport()`, which calls `LegacyDeviceRepository`, returning report to Java UI. | PASS |
| T8 | Unit tests execution | `DeviceAnalyzerTest` unit tests pass (5 passed). | PASS |

## Expected Summary Report Output
```
DEVICE REPORT
Camera-C (Entrance camera) | Camera | unknown
Gateway-E | Gateway | invalid (140)
Sensor-A (Laboratory sensor) | Sensor | 15%
Tracker-D | Tracker | 40%
Unnamed device | Gateway | 85%

Total: 5
Valid batteries: 3
Low batteries: 1
Invalid or missing: 2
Average: 46.7%
```
