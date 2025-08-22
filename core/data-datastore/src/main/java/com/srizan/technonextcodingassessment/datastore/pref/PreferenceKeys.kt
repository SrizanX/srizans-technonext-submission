package com.srizan.technonextcodingassessment.datastore.pref

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object PreferenceKeys {
    val userEmail = intPreferencesKey("user_email")
    val isUserLoggedIn = booleanPreferencesKey("isUserLoggedIn")
}