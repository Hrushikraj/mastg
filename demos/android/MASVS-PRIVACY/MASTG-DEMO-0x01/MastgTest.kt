package org.owasp.mastestapp

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// SUMMARY: This sample demonstrates an app that declares excessive dangerous permissions
// that are not required for its core functionality, violating the principle of least privilege.

class MastgTest(private val context: Context) {

    val shouldRunInMainThread = true

    fun mastgTest(): String {
        val r = DemoResults("0000")

        val dangerousPermissions = arrayOf(
            Manifest.permission.READ_CALENDAR,        // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.CAMERA,                // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.READ_CONTACTS,         // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.ACCESS_FINE_LOCATION,  // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.ACCESS_COARSE_LOCATION,// FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.RECORD_AUDIO,          // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.READ_PHONE_STATE,      // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.BODY_SENSORS,          // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.SEND_SMS,              // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.ACTIVITY_RECOGNITION,  // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.POST_NOTIFICATIONS,    // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.NEARBY_WIFI_DEVICES,   // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.BLUETOOTH_CONNECT,     // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
            Manifest.permission.READ_MEDIA_IMAGES,     // FAIL: [MASTG-TEST-0x01] Dangerous permission not justified by app functionality.
        )

        val activity = context as Activity

        // Request all ungranted permissions (triggers runtime dialogs)
        val ungranted = dangerousPermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (ungranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, ungranted.toTypedArray(), 1001)
        }

        // Report current status of each permission
        for (perm in dangerousPermissions) {
            val shortName = perm.substringAfterLast(".")
            val granted = ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
            r.add(
                Status.FAIL,
                "Dangerous permission $shortName: ${if (granted) "GRANTED" else "DENIED"}"
            )
        }

        return r.toJson()
    }
}
