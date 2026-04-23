---
platform: android
title: Excessive Dangerous Permissions in the AndroidManifest with semgrep
id: MASTG-DEMO-0x01
code: [kotlin]
test: MASTG-TEST-0x01
---

## Sample

This demo demonstrates an Android application that declares **14 dangerous permissions** that are not required for its core functionality, violating the principle of least privilege. The app is a simple test application with no features that would justify any of these permissions.

The following dangerous permissions are declared in the AndroidManifest:

| Permission | Category | Why It's Excessive |
|------------|----------|-------------------|
| `READ_CALENDAR` | Calendar | App has no calendar functionality |
| `CAMERA` | Camera | App has no photo/video features |
| `READ_CONTACTS` | Contacts | App has no contact integration |
| `ACCESS_FINE_LOCATION` | Location | App has no location-based features |
| `ACCESS_COARSE_LOCATION` | Location | App has no location-based features |
| `RECORD_AUDIO` | Microphone | App has no audio recording |
| `READ_PHONE_STATE` | Phone | App has no telephony features |
| `BODY_SENSORS` | Sensors | App has no health/fitness features |
| `SEND_SMS` | SMS | App has no messaging features |
| `ACTIVITY_RECOGNITION` | Activity | App has no fitness tracking |
| `POST_NOTIFICATIONS` | Notifications | App has no notification features |
| `NEARBY_WIFI_DEVICES` | Nearby Devices | App has no device discovery |
| `BLUETOOTH_CONNECT` | Nearby Devices | App has no Bluetooth features |
| `READ_MEDIA_IMAGES` | Storage | App has no media gallery features |

{{ AndroidManifest.xml # AndroidManifest_reversed.xml }}

The `MastgTest.kt` file demonstrates how the app requests these permissions at runtime and reports their grant status. Note the FAIL comments indicating each permission is not justified by app functionality.

{{ MastgTest.kt }}

## Steps

We will use static analysis to detect all dangerous permissions declared in the AndroidManifest.xml file. This approach is useful because:

1. It can be automated as part of CI/CD pipelines
2. It doesn't require running the app
3. It can scan APKs without source code access (using the reversed/decompiled manifest)

Let's run our @MASTG-TOOL-0110 rule against the reversed manifest file. The rule checks for all known dangerous Android permissions.

{{ ../../../../rules/mastg-android-excessive-dangerous-permissions.yaml }}

{{ run.sh }}

## Observation

The rule has identified **14 instances** in the AndroidManifest file where the app declares dangerous permissions. Each finding shows:

- The line number in the reversed manifest
- The permission name (e.g., `android.permission.CAMERA`)
- The MASVS control tag (`[MASVS-PRIVACY-1]`)

{{ output.txt }}

## Evaluation

The test **fails** because the app declares 14 dangerous permissions without any functional justification:

**Calendar Permissions:**
- `READ_CALENDAR` - No calendar features exist in the app

**Camera Permissions:**
- `CAMERA` - No photo or video capture functionality

**Contacts Permissions:**
- `READ_CONTACTS` - No contact list integration

**Location Permissions:**
- `ACCESS_FINE_LOCATION` - No GPS or location-based features
- `ACCESS_COARSE_LOCATION` - No location-based features

**Microphone Permissions:**
- `RECORD_AUDIO` - No voice recording or audio input

**Phone Permissions:**
- `READ_PHONE_STATE` - No telephony features or call handling

**Sensor Permissions:**
- `BODY_SENSORS` - No health or fitness tracking

**SMS Permissions:**
- `SEND_SMS` - No messaging functionality

**Activity Recognition:**
- `ACTIVITY_RECOGNITION` - No fitness or movement tracking

**Notification Permissions:**
- `POST_NOTIFICATIONS` - No push notification features

**Nearby Device Permissions:**
- `NEARBY_WIFI_DEVICES` - No device discovery or WiFi Direct
- `BLUETOOTH_CONNECT` - No Bluetooth connectivity features

**Storage Permissions:**
- `READ_MEDIA_IMAGES` - No photo gallery or media browsing

**Remediation:**

All 14 dangerous permissions should be removed from the AndroidManifest.xml to comply with the principle of least privilege. For each permission an app needs, developers should:

1. **Evaluate necessity**: Does the app truly need this permission?
2. **Consider alternatives**: Can the functionality be achieved without the permission (e.g., using system intents)?
3. **Document justification**: If the permission is needed, document why
4. **Request at runtime**: Only request dangerous permissions when the feature is actually used

See the [Android documentation on minimizing permission requests](https://developer.android.com/privacy-and-security/minimize-permission-requests) for privacy-preserving alternatives.
