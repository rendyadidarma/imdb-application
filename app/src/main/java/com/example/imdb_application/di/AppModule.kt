package com.example.imdb_application.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.imdb_application.data.local.database.MovieDao
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.remote.api.APIService
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.repository.MovieRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): APIService {
        return Retrofit.Builder()
            .baseUrl("https://imdb-api.com/")
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder().add(
                    KotlinJsonAdapterFactory()
                ).build()))
            .build()
            .create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDao(movieDatabase: MovieDatabase) : MovieDao {
        return movieDatabase.movieDao
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context) : MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java, "items")
                .fallbackToDestructiveMigration()
                .build()
    }

//    @Provides
//    @Singleton
//    fun provideRepository(databaseDao : MovieDao, network : APIService, @ApplicationContext context: Context) : MovieRepository {
//        return MovieRepositoryImpl(databaseDao, network, context)
//    }

    @Provides
    @Singleton
    fun provideContext(application: Application) : Context {
        return application.applicationContext
    }

}