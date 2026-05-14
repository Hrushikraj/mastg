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

The test fails if there are any dangerous permissions in the app.

Compare the list of declared permissions with the list of [dangerous permissions](https://android.googlesource.com/platform/frameworks/base/%2B/master/core/res/AndroidManifest.xml) defined by Android. You can find more details in the [Android documentation](https://developer.android.com/reference/android/Manifest.permission).
