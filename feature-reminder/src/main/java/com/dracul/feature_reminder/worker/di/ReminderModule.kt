package com.dracul.feature_reminder.worker.di

import org.koin.dsl.module
import kotlin.reflect.KClass


fun reminderModule(clazz: KClass<*>) =   module {
    single<MProvider> {
        MProvider(clazz)
    }
}

class MProvider(
    val clazz: KClass<*>
)