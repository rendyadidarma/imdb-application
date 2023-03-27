package com.example.imdb_application.data.repository

import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.state.NetworkResponseWrapper
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(isDatabaseEmpty: Boolean): Flow<NetworkResponseWrapper<List<Movie>>>
    suspend fun getMoviesFromNetwork(): List<Movie>
    suspend fun getDetail(
        isDetailEmpty: Boolean,
        id: String
    ): Flow<NetworkResponseWrapper<MovieDetail>>

    suspend fun getDetailNotReturnNetworkState(
        isDetailEmpty: Boolean,
        id: String
    ): Flow<MovieDetail?>

    suspend fun refreshDetail(id: String)
    fun isDetailEmpty(id: String): Flow<Boolean>
    fun isDatabaseEmpty(): Flow<Boolean>
    suspend fun searchMovies(keyword: String): NetworkResponseWrapper<List<Movie>>
}