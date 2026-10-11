# Device Readiness Checker (CSC303 Lab 05)

A single-screen Jetpack Compose application built with Material 3 that evaluates device deployment readiness based on name, battery percentage, and network availability.


## 1. Validation and Readiness Rules

| Field or Decision    | Rule |
|:---------------------| :--- |
| Device name          | Required after trimming; blank input is invalid. |
| Battery percentage** | Whole number from 0 through 100; non-numeric, missing, and out-of-range inputs are invalid. |
| Network available    | Boolean selected via Material 3 `Switch`. |
| Ready                | Valid form, battery $\ge$ 30%, and network available. |
| Needs attention      | Valid form, but battery < 30% or network unavailable. |
| After input changes  | Hides the previous result so stale evaluation data is not displayed. |

---

## 2. State and Event Map

| State Variable | Owned / Displayed By | Changed By Event |
| :--- | :--- | :--- |
| `deviceName` | `DeviceReadinessApp` / `OutlinedTextField` & Result Card | `onDeviceNameChange`, `onClear` |
| `batteryText` | `DeviceReadinessApp` / `OutlinedTextField` & Progress Bar | `onBatteryChange`, `onClear` |
| `networkAvailable` | `DeviceReadinessApp` / Material `Switch` | `onNetworkChange`, `onClear` |
| `submitted` | `DeviceReadinessApp` / Error & Card Visibility | `onEvaluate` (`true`), Input changes (`false`), `onClear` (`false`) |

---

## 3. Verification Test Results (T1 – T8)

| ID | Input or Action | Expected Behavior | Actual Behavior | Result |
| :---: | :--- | :--- | :--- | :---: |
| **T1** | **Clean build & launch** | App builds and opens with top bar, form fields, switch, and buttons. | TopAppBar, intro text, Device Name, Battery percentage, Network switch, and Evaluate/Clear buttons rendered. | **PASS** |
| **T2** | **Evaluate blank form** | Both validation error messages appear; no result card shown. | "Device name is required" and "Enter a valid battery percentage from 0 to 100" visible. Result card hidden. | **PASS** |
| **T3** | **Battery text, -1, or 101** | Battery error appears; form remains active and editable. | Displays "Enter a valid battery percentage from 0 to 100". Fields remain editable. | **PASS** |
| **T4** | **Battery 0, 29, 30, 100 (Network ON)** | 0/29 show "Needs attention"; 30/100 show "Ready". | 0 & 29 display "Needs attention". 30 & 100 display "Ready". | **PASS** |
| **T5** | **Battery 80; toggle network OFF** | Result updates to "Needs attention" after re-evaluation. | Toggling switch OFF resets `submitted`, hiding old result. Re-evaluating updates result to "Needs attention". | **PASS** |
| **T6** | **Evaluate valid values, then edit field** | Result card disappears to avoid stale information. | Editing `deviceName` triggers `onDeviceNameChange` setting `submitted = false`, immediately hiding old result. | **PASS** |
| **T7** | **Enter values and recreate Activity** | Form state is preserved via `rememberSaveable`. | `deviceName`, `batteryText`, `networkAvailable`, and `submitted` restored after device rotation. | **PASS** |
| **T8** | **Font scaling & accessibility focus** | Content remains reachable and TalkBack announcements are meaningful. | Scrollable `Column` with `imePadding()` prevents clipping. TalkBack reads full merged description cleanly. | **PASS** |

---

## 4. Accessibility Review

1. **Focus Order:** Sequential focus flows naturally from `TopAppBar` $\rightarrow$ `Intro Text` $\rightarrow$ `Device Name` $\rightarrow$ `Battery` $\rightarrow$ `Network Switch` $\rightarrow$ `Evaluate` $\rightarrow$ `Clear` $\rightarrow$ `Result Card`.
2. **Visible Labels & Non-Color Validation:** All inputs feature visible labels. Validation errors are explicitly stated in supporting text.
3. **Switch Role & Description:** Switch exposes toggle state and custom description (`"Change network availability"`).
4. **Merged Semantics Tree:** Result card combines status, message, battery level, and network into a single announcement (`"Device Sensor-A. Battery 80 percent. Network available. Status Ready."`).
5. **Font Scaling:** Layout remains scrollable and unclipped under increased font scale.

---

## 5. Automated Compose UI Tests

Located in [`DeviceReadinessScreenTest.kt`](file:///D:/Mobile%20Application%20Development/lab05-device-readiness/app/src/androidTest/java/com/example/devicereadiness/DeviceReadinessScreenTest.kt):
- `blankForm_showsBothValidationMessages()`: Validates that clicking Evaluate on empty inputs displays both required error messages.
- `validForm_showsReadyResult()`: Fills valid inputs ("Sensor-A", "80"), clicks Evaluate, and asserts the ready result card is displayed.
