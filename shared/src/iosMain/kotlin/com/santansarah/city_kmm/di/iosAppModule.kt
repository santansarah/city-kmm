package com.santansarah.city_kmm.di

import com.santansarah.city_kmm.Greeting
import com.santansarah.city_kmm.data.local.UserRepository
import com.santansarah.city_kmm.utils.provideDatastore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module


val iosDatastoreModule = module {
    provideDatastore(null)
}


class UserPreferencesHelper : KoinComponent {
   val repo: UserRepository by inject<UserRepository>()
}


fun initKoin(){
    startKoin {
        modules(iosDatastoreModule, commonModule)
    }
}