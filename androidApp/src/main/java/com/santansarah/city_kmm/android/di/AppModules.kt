package com.santansarah.city_kmm.android.di

import com.santansarah.city_kmm.utils.provideDatastore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDatastoreModule = module {

    single {
        provideDatastore(androidContext())
    }
}