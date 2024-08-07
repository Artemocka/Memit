package com.dracul.feature_reminder.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dracul.common.aliases.CommonDrawables

class ReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val message = inputData.getString("MESSAGE") ?: return Result.failure()
        sendReminder(message)
        return Result.success()
    }
    private fun sendReminder(message: String) {
        val builder = NotificationCompat.Builder(applicationContext, "reminderChannel")
            .setSmallIcon(CommonDrawables.notification_icon)
            .setContentTitle("Reminder")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }
}