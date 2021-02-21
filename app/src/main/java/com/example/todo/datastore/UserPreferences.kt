package com.example.todo.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferences(context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "my_data_store")

    val authToken : Flow<String?>
        get() = dataStore.data.map {
            it[KEY_AUTH]
        }

    suspend fun saveAuthToken(token : String){
        dataStore.edit {
            it[KEY_AUTH] = token
        }
    }

    suspend fun getAuthToken() : String?{
        val preferences = dataStore.data.first()
        return preferences[KEY_AUTH]
    }



    suspend fun clearToken(){
        dataStore.edit {
            it.clear()
        }
    }

    companion object{
        private val KEY_AUTH = preferencesKey<String>("key_auth")
    }
}