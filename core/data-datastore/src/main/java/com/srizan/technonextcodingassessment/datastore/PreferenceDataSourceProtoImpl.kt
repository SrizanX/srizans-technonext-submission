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
    fileName = "app_prefs.pb", 
    serializer = AppsProtoPrefsSerializer
)

/**
 * Implementation of PreferenceDataSource using Protocol Buffers with DataStore.
 * 
 * This class provides type-safe, efficient storage for application preferences
 * using Protocol Buffers serialization. It handles user authentication state,
 * theme preferences, and other app-level configurations.
 * 
 * Benefits of Proto DataStore over SharedPreferences:
 * - Type safety with compile-time guarantees
 * - Better performance for complex data structures  
 * - Atomic read/write operations
 * - Built-in data migration support
 */
class PreferenceDataSourceProtoImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PreferenceDataSource {
    /**
     * Retrieves the user's email address from preferences.
     * @return Flow<String?> The user's email, or null if not set
     */
    override fun getUserEmail() = context.appsProtoPrefs.data.map {
        it.userEmail
    }

    /**
     * Stores the user's email address in preferences.
     * @param email The email address to store
     */
    override suspend fun setUserEmail(email: String) {
        context.appsProtoPrefs.updateData { currentSettings ->
            currentSettings.toBuilder().setUserEmail(email).build()
        }
    }

    /**
     * Retrieves the current app theme configuration.
     * Maps from Proto enum to domain model enum.
     * @return Flow<AppThemeConfig> The current theme configuration
     */
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

    /**
     * Updates the app theme configuration.
     * Maps from domain model enum to Proto enum.
     * @param themeConfig The new theme configuration to apply
     */
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

    /**
     * Clears all stored preferences.
     * This is typically called during user logout.
     */
    override suspend fun clearPreferences() {
        context.appsProtoPrefs.updateData { currentSettings ->
            currentSettings.toBuilder().clear().build()
        }
    }

    /**
     * Checks if the user is currently logged in.
     * @return Flow<Boolean?> The login status, or null if not set
     */
    override fun isUserLoggedIn() = context.appsProtoPrefs.data.map {
        it.isUserLoggedIn
    }

    /**
     * Updates the user's login status.
     * @param loggedIn true if user is logged in, false otherwise
     */
    override suspend fun setUserLoggedInStatus(loggedIn: Boolean) {
        context.appsProtoPrefs.updateData { currentSettings ->
            currentSettings.toBuilder().setIsUserLoggedIn(loggedIn).build()
        }
    }
}



