package com.santansarah.city_kmm.di

import com.santansarah.city_kmm.data.local.UserRepository
import com.santansarah.city_kmm.remote.apis.UserApiService
import com.santansarah.city_kmm.utils.provideDispatcher
import org.koin.dsl.module

val commonModule = module {

    single { provideDispatcher() }
    single { UserApiService() }
    single { UserRepository(get(), get(), get()) }

}