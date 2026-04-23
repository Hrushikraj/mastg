---
title: Excessive Dangerous App Permissions
platform: android
id: MASTG-TEST-0x01
type: [static]
weakness: MASWE-0117
profiles: [P]
knowledge: [MASTG-KNOW-0017]
---

## Overview

Android apps must declare permissions in the `AndroidManifest.xml` file using `<uses-permission>` tags to access protected resources and sensitive user data. Permissions are classified into different protection levels, with **dangerous permissions** requiring explicit user consent at runtime because they grant access to sensitive information such as contacts, location, camera, microphone, SMS, and phone state.

When an app declares excessive dangerous permissions that are not required for its core functionality, it violates the **principle of least privilege**. This can lead to:

- Unnecessary exposure of user data if the app is compromised
- Increased attack surface for malicious actors
- User distrust and potential rejection of permission requests
- Play Store policy violations for restricted permissions

The following is a complete list of dangerous permissions grouped by category:

| Category | Permissions |
|----------|-------------|
| Calendar | `READ_CALENDAR`, `WRITE_CALENDAR` |
| Call Log | `READ_CALL_LOG`, `WRITE_CALL_LOG`, `PROCESS_OUTGOING_CALLS` |
| Camera | `CAMERA` |
| Contacts | `READ_CONTACTS`, `WRITE_CONTACTS`, `GET_ACCOUNTS` |
| Location | `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `ACCESS_BACKGROUND_LOCATION` |
| Microphone | `RECORD_AUDIO` |
| Phone | `READ_PHONE_STATE`, `READ_PHONE_NUMBERS`, `CALL_PHONE`, `ANSWER_PHONE_CALLS`, `ADD_VOICEMAIL`, `USE_SIP`, `ACCEPT_HANDOVER` |
| Sensors | `BODY_SENSORS`, `BODY_SENSORS_BACKGROUND` |
| SMS | `SEND_SMS`, `RECEIVE_SMS`, `READ_SMS`, `RECEIVE_WAP_PUSH`, `RECEIVE_MMS` |
| Storage | `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`, `READ_MEDIA_IMAGES`, `READ_MEDIA_VIDEO`, `READ_MEDIA_AUDIO`, `READ_MEDIA_VISUAL_USER_SELECTED`, `ACCESS_MEDIA_LOCATION` |
| Activity Recognition | `ACTIVITY_RECOGNITION` |
| Nearby Devices | `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, `BLUETOOTH_ADVERTISE`, `NEARBY_WIFI_DEVICES`, `UWB_RANGING` |
| Notifications | `POST_NOTIFICATIONS` |

This test checks whether the app declares dangerous permissions that violate the principle of least privilege.

## Steps

There are multiple tools that can help in finding permissions declared by an app. Refer to @MASTG-TECH-0118 and use any of the mentioned tools.

1. Extract the `AndroidManifest.xml` file from the APK (see @MASTG-TECH-0117).
2. Obtain the list of declared permissions (see @MASTG-TECH-0126).

Alternatively, you can use `aapt` to dump permissions directly from the APK:

```bash
aapt d permissions app-debug.apk
```

Or use `adb` to examine permissions of an installed app:

```bash
adb shell dumpsys package <package_name> | grep permission
```

## Observation

The output should contain the list of dangerous permissions declared by the app, including:

- The permission name (e.g., `android.permission.CAMERA`)
- The permission group it belongs to (e.g., Camera, Location, Contacts)
- Whether it is a dangerous permission requiring runtime consent

## Evaluation

The test case fails if the app declares dangerous permissions that are not justified by its core functionality.

Compare the list of declared permissions with the list of [dangerous permissions](https://android.googlesource.com/platform/frameworks/base/%2B/master/core/res/AndroidManifest.xml) defined by Android. Each dangerous permission must be justified by a corresponding feature in the app. You can find more details in the [Android documentation](https://developer.android.com/reference/android/Manifest.permission).

**Context Consideration**:

Context is essential when evaluating permissions. For example:

- An app that uses the camera to scan QR codes should have the `CAMERA` permission
- A navigation app legitimately needs `ACCESS_FINE_LOCATION`
- A messaging app may need `READ_CONTACTS` to show contact names

However, if an app does not have a feature that requires a permission, that permission is unnecessary and should be removed.

**Privacy-Preserving Alternatives**:

Consider if there are privacy-preserving alternatives to the permissions used by the app:

- Instead of `CAMERA`, use the device's built-in camera app via `ACTION_IMAGE_CAPTURE` or `ACTION_VIDEO_CAPTURE` intents
- Instead of `ACCESS_FINE_LOCATION`, consider if `ACCESS_COARSE_LOCATION` is sufficient, or ask the user to enter a postal code manually
- Instead of `READ_CONTACTS`, let users manually select contacts using the system contact picker
- Instead of `BLUETOOTH_ADMIN` and location permissions, use [Companion Device Pairing](https://developer.android.com/guide/topics/connectivity/companion-device-pairing) (Android 8.0+)
- Instead of SMS permissions for verification, use the [SMS Retriever API](https://developers.google.com/identity/sms-retriever/overview)

These alternatives allow the app to access functionality without directly requesting dangerous permissions, thereby enhancing user privacy and reducing the attack surface.
