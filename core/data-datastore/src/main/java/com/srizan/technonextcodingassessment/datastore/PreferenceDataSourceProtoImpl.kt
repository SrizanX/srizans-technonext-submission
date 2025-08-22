package com.srizan.technonextcodingassessment.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.srizan.technonextcodingassessment.domain.datasource.PreferenceDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.appsProtoPrefs: DataStore<AppsProtoPrefs> by dataStore(
    fileName = "app_prefs.pb", serializer = AppsProtoPrefsSerializer
)

class PreferenceDataSourceProtoImpl @Inject constructor(
    @ApplicationContext val context: Context
) : PreferenceDataSource {
    /**
     * Get the user name from the data store.
     */
    override fun getUserEmail() = context.appsProtoPrefs.data.map {
        it.userEmail
    }

    override suspend fun setUserEmail(email: String) {
        context.appsProtoPrefs.updateData { currentSettings ->
            currentSettings.toBuilder().setUserEmail(email).build()
        }
    }

    override suspend fun clearPreferences() {
        context.appsProtoPrefs.updateData { currentSettings ->
            currentSettings.toBuilder().clear().build()
        }
    }

    override fun isUserLoggedIn() = context.appsProtoPrefs.data.map {
        it.isUserLoggedIn
    }

    override suspend fun setUserLoggedInStatus(loggedIn: Boolean) {
        context.appsProtoPrefs.updateData { currentSettings ->
            currentSettings.toBuilder().setIsUserLoggedIn(loggedIn).build()
        }
    }
}



