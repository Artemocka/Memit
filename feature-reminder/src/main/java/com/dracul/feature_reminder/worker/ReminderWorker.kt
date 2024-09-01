package com.dracul.feature_reminder.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dracul.common.aliases.CommonDrawables
import com.dracul.feature_reminder.worker.di.MProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val NOTE_ID = "NOTE_ID"
const val MESSAGE = "MESSAGE"

class ReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {
    val mProvider: MProvider by inject()
    override fun doWork(): Result {
        val message = inputData.getString(MESSAGE) ?: return Result.failure()
        val noteId = inputData.getLong(NOTE_ID, -1)
        sendReminder(message, noteId)
        return Result.success()
    }

    private fun sendReminder(message: String, noteId: Long) {
        val intent = Intent(applicationContext, mProvider.clazz.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(NOTE_ID, noteId)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, "reminderChannel")
            .setSmallIcon(CommonDrawables.notification_icon).setContentTitle("Reminder")
            .setContentText(message).setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(noteId.toInt(), builder.build())
        }
    }
}