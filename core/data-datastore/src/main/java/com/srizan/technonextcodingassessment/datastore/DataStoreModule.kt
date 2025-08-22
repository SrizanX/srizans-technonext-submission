package com.srizan.technonextcodingassessment.datastore

import android.content.Context
import com.srizan.technonextcodingassessment.datastore.pref.DataStorePrefsHelper
import com.srizan.technonextcodingassessment.domain.datasource.PreferenceDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Binds
    abstract fun bindPreferenceDataSource(
        impl: PreferenceDataSourceProtoImpl
    ): PreferenceDataSource
}


@Module
@InstallIn(SingletonComponent::class)
object DataStorePrefHelperModule {

    @Provides
    @Singleton
    fun provideDataStorePrefsHelper(
        @ApplicationContext context: Context,
    ): DataStorePrefsHelper {
        return DataStorePrefsHelper(context)
    }
}
