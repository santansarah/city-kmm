package com.santansarah.city_kmm.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.santansarah.city_kmm.data.local.dataStoreFileName
import com.santansarah.city_kmm.data.local.getDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class AndroidDispatcher: Dispatcher{
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
}

internal actual fun provideDispatcher(): Dispatcher = AndroidDispatcher()

// we'll need to pass the context down from Android
actual fun provideDatastore(context: Any?): DataStore<Preferences> = getDataStore(
    producePath = {
        (context as? Context)?.let {
            context.filesDir.resolve(dataStoreFileName).absolutePath
        } ?: throw Exception("Couldn't get Android Datastore context.")
    }
)