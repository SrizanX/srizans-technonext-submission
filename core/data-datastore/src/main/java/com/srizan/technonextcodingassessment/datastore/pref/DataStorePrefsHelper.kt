package com.srizan.technonextcodingassessment.datastore.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

/**
 * @author srizan
 * */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preference_data_store")

class DataStorePrefsHelper(
    private val context: Context
) {

    fun <T> getPreference(key: Preferences.Key<T>) = context.dataStore.data.map {
        it[key]
    }

    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[key] = value
        }
    }

    suspend fun resetPreferences() {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences.clear()
        }
    }

    suspend fun <T> removePreference(key: Preferences.Key<T>) =
        context.dataStore.edit { it.remove(key) }
}
