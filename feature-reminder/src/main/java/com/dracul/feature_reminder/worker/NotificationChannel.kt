package com.dracul.feature_reminder.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

fun createNotificationChannel(applicationContext: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Reminder Channel"
        val descriptionText = "Channel for reminders"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("reminderChannel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}