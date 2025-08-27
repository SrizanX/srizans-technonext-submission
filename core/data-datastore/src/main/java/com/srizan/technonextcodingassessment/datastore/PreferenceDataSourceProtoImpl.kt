package com.srizan.technonextcodingassessment.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.srizan.technonextcodingassessment.domain.datasource.PreferenceDataSource
import com.srizan.technonextcodingassessment.model.AppThemeConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.srizan.technonextcodingassessment.datastore.AppThemeConfig as ProtoAppThemeConfig


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

    override fun getAppThemeConfig(): Flow<AppThemeConfig> {
        return context.appsProtoPrefs.data.map {
            when (it.appThemeConfig) {
                ProtoAppThemeConfig.LIGHT -> AppThemeConfig.LIGHT
                ProtoAppThemeConfig.DARK -> AppThemeConfig.DARK
                ProtoAppThemeConfig.SYSTEM -> AppThemeConfig.SYSTEM
                ProtoAppThemeConfig.UNRECOGNIZED -> AppThemeConfig.SYSTEM
            }
        }
    }

    override suspend fun setAppThemeConfig(themeConfig: AppThemeConfig) {
        context.appsProtoPrefs.updateData { currentSettings ->
            val protoThemeConfig = when (themeConfig) {
                AppThemeConfig.LIGHT -> ProtoAppThemeConfig.LIGHT
                AppThemeConfig.DARK -> ProtoAppThemeConfig.DARK
                AppThemeConfig.SYSTEM -> ProtoAppThemeConfig.SYSTEM
            }
            currentSettings.toBuilder().setAppThemeConfig(protoThemeConfig).build()
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



