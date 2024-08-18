package com.dracul.notes

import android.app.Application
import com.dracul.feature_reminder.worker.di.reminderModule
import com.dracul.notes.di.notesModule
import org.koin.core.context.startKoin


class App:Application() {
    val koin = startKoin{
        modules(
            notesModule,
            reminderModule(MainActivity::class)
        )
    }
}