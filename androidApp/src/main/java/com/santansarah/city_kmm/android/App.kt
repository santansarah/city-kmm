package com.santansarah.city_kmm.android

import android.app.Application
import com.santansarah.city_kmm.android.di.androidDatastoreModule
import com.santansarah.city_kmm.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CityKMMApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@CityKMMApp)
            // Load modules
            modules(androidDatastoreModule, commonModule)
        }

    }
}

