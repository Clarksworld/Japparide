package com.japaride.app.network.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences (
    context: Context
) {



    private val applicationContext = context.applicationContext

    private val dataStore: DataStore<Preferences>

    init {

        dataStore = applicationContext.createDataStore(
            name = "my_data_store"
        )


    }

    val authToken: Flow<String?>
        get() = dataStore.data.map { prefereces ->
        prefereces[KEY_AUTH]
    }

    val userId: Flow<String?>
        get() = dataStore.data.map { preference->
        preference[USER_ID]
    }

    val password: Flow<String?>
        get() = dataStore.data.map { preference->
        preference[PASSWORD]
    }



    suspend fun safeAuthToken(authToken: String){
        dataStore.edit { preference ->

            preference[KEY_AUTH] = authToken
        }

    }


    suspend fun saveUserId(userId: String){
        dataStore.edit { preference ->

            preference[USER_ID] = userId
        }

    }

    suspend fun saveUserPassword(password: String){
        dataStore.edit { preference ->

            preference[PASSWORD] = password

        }

    }





    companion object{

        private val KEY_AUTH = preferencesKey<String>("key_auth")
        private val USER_ID = preferencesKey<String>("user_id")
        private val PASSWORD = preferencesKey<String>("password")


    }
}