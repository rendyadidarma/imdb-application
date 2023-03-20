package com.example.imdb_application.data.repository

import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.state.NetworkResponseWrapper
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies() : Flow<List<Movie>>?
    suspend fun getMoviesFromNetwork(): List<Movie>
    suspend fun getDetail(id: String): Flow<NetworkResponseWrapper<MovieDetail>>
    suspend fun refreshDetail(id : String)
    fun isDatabaseEmpty() : Flow<Boolean>
    suspend fun searchMovies(keyword : String): List<Movie>
}