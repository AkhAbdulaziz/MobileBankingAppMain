package uz.targetsoftwaredevelopment.mobilebankingapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.util.ArrayList

fun Activity.checkPermission(permission: String, granted : () -> Unit) {
    Permissions.check(
        this, permission, null,
        permissionHandler(granted,
            {
                Toast.makeText(this,"denied", Toast.LENGTH_SHORT).show()
                goToSettings()
            },
            {
                Toast.makeText(this,"block", Toast.LENGTH_SHORT).show()
                goToSettings()
            })
    )
}

const val REQUEST_APP_SETTINGS = 11001
fun Activity.goToSettings() {
    val intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivityForResult(intent, REQUEST_APP_SETTINGS)
}

private fun permissionHandler(
    granted: () -> Unit,
    denied: () -> Unit,
    block: () -> Unit
) : PermissionHandler {
    return object : PermissionHandler() {
        override fun onGranted() {
            granted.invoke()
        }

        override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
            super.onDenied(context, deniedPermissions)
            denied.invoke()
        }

        override fun onBlocked(context: Context?, blockedList: ArrayList<String>?): Boolean {
            block.invoke()
            return super.onBlocked(context, blockedList)
        }

        override fun onJustBlocked(context: Context?, justBlockedList: ArrayList<String>?, deniedPermissions: ArrayList<String>?) {
            super.onJustBlocked(context, justBlockedList, deniedPermissions)
            block.invoke()
        }
    }
}