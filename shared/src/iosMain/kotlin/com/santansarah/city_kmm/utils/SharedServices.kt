package com.santansarah.city_kmm.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.santansarah.city_kmm.data.local.dataStoreFileName
import com.santansarah.city_kmm.data.local.getDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

internal class IosDispatcher: Dispatcher{
    override val io: CoroutineDispatcher
        get() = Dispatchers.Default
}

internal actual fun provideDispatcher(): Dispatcher = IosDispatcher()

actual fun provideDatastore(context: Any?): DataStore<Preferences> = getDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/$dataStoreFileName"
    }
)
