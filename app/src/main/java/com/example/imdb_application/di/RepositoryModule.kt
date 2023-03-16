package com.example.imdb_application.di

import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        movieRepositoryImpl : MovieRepositoryImpl
    ) : MovieRepository

}