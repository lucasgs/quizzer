package com.dendron.quizzer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.dendron.quizzer.common.Constants
import com.dendron.quizzer.common.Constants.DATA_STORE_NAME
import com.dendron.quizzer.domain.repository.SettingsRepository
import com.dendron.quizzer.domain.repository.TriviaRepository
import com.dendron.quizzer.local.LocalSettingsRepository
import com.dendron.quizzer.remote.OpenTriviaDbApi
import com.dendron.quizzer.remote.OpenTriviaDbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideOpenTriviaDbApi(): OpenTriviaDbApi {
        val moviesClient = OkHttpClient().newBuilder()
            .build()

        return Retrofit.Builder()
            .client(moviesClient)
            .baseUrl(Constants.OPENTRIVIADB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenTriviaDbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTriviaRepository(
        api: OpenTriviaDbApi
    ): TriviaRepository {
        return OpenTriviaDbRepository(api)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: DataStore<Preferences>): SettingsRepository {
        return LocalSettingsRepository(dataStore)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile(DATA_STORE_NAME)
            }
        )
    }
}