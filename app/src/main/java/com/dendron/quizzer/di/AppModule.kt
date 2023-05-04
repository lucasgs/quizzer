package com.dendron.quizzer.di

import com.dendron.quizzer.common.Constants
import com.dendron.quizzer.domain.repository.TriviaRepository
import com.dendron.quizzer.remote.OpenTriviaDbApi
import com.dendron.quizzer.remote.OpenTriviaDbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
            .baseUrl(Constants.OPENTRIVIDB_BASE_URL)
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
}