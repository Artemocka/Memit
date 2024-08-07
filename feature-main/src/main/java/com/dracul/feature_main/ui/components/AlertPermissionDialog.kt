package com.dracul.feature_main.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity


@Composable
fun AlertPermissionDialog(
    context: Context, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(
                text = "Permission", fontWeight = FontWeight.Bold, fontSize = 16.sp
            )
        },
        text = {
            Text(
                "The notification is important for this app. Please grant the permission.",
                fontSize = 16.sp
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                } else {
                    Intent(
                        Settings.ACTION_APPLICATION_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(context, intent, null)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text("Cancel")
            }
        },
    )
}