package com.example.imdb_application.di.modules

import android.content.Context
import androidx.room.Room
import com.example.imdb_application.data.local.database.MovieDao
import com.example.imdb_application.data.local.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
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
}