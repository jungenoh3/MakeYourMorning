package com.yesnoheun3.makeyourmorning.utilities.accessibility

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast

object AccessibilityServiceChecker {
    fun isAccessibilityServiceEnabled(context: Context, service: Class<*>): Boolean {
        val expectedComponentName = "${context.packageName}/${service.name}"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (enabledServices.isNullOrEmpty()) {
            return false
        }
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServices)
        while (colonSplitter.hasNext()) {
            val serviceName = colonSplitter.next()
            if (serviceName.equals(expectedComponentName, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    fun requestAccessibilityPermissionIfNeeded(context: Context, service: Class<*>) {
        if (!isAccessibilityServiceEnabled(context, service)) {
            Toast.makeText(context, "접근성 권한이 필요합니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "접근성 권한이 이미 활성화되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}