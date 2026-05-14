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


## Steps

There are multiple tools that can help in finding permissions in use by an app. Refer @MASTG-TECH-0118 to and use any of the mentioned tools.

1. Extract the `AndroidManifest.xml` file from the APK (see @MASTG-TECH-0117).
2. Obtain the list of declared permissions (see @MASTG-TECH-0126).

## Observation

The output should contain the list of permissions declared by the app.

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
