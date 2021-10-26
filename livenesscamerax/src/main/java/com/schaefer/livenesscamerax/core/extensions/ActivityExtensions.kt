package com.schaefer.livenesscamerax.core.extensions

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.schaefer.livenesscamerax.domain.model.PermissionsType

internal fun Activity.getPermissionStatus(androidPermissionName: String): PermissionsType {
    return if (ContextCompat.checkSelfPermission(
            this,
            androidPermissionName
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                androidPermissionName
            ).not()
        ) {
            PermissionsType.BLOCKED_OR_NEVER_ASKED
        } else PermissionsType.DENIED
    } else PermissionsType.GRANTED
}

internal fun Activity.shouldShowRequest(permission: String): Boolean {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        shouldShowRequestPermissionRationale(permission)
    } else {
        when (getPermissionStatus(permission)) {
            PermissionsType.GRANTED -> false
            PermissionsType.DENIED,
            PermissionsType.BLOCKED_OR_NEVER_ASKED -> true
        }
    }
}

internal fun Activity.sendResultAndFinish(activityResult: Int, intent: Intent) {
    setResult(activityResult, intent)
    finish()
}
