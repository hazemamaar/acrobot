package com.example.acrobot.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

const val PREFERENCE_NAME = "acrobot-datastore"

class PreDataStore @Inject constructor(context: Context) {
    private object PreferenceKeys {
        val firstOpen = preferencesKey<Boolean>("first_open")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = PREFERENCE_NAME)

    suspend fun saveToDataStore(name: Boolean) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.firstOpen] = name
        }
    }

    val readFromDataStore: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val myName = preference[PreferenceKeys.firstOpen] ?: false
            myName
        }

}