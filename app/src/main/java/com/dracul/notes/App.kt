package com.dracul.notes

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.dracul.feature_reminder.worker.di.reminderModule
import com.dracul.images.di.imagesModule
import com.dracul.notes.di.notesModule
import org.koin.core.context.startKoin


class App : Application(), ImageLoaderFactory {
    val koin = startKoin {
        modules(
            notesModule,
            imagesModule,
            reminderModule(MainActivity::class)
        )
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }
}