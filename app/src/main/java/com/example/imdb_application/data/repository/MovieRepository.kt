package com.example.imdb_application.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.remote.api.APINetwork
import com.example.imdb_application.data.remote.dto.MovieDtoSearch
import com.example.imdb_application.data.utils.MovieObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface MovieRepository {
    suspend fun getMovies() : Flow<List<Movie>>?
    suspend fun getMoviesFromNetwork(): List<Movie>
    suspend fun getDetailFromDatabase(id: String): Flow<MovieDetail>?
    suspend fun getDetailFromNetwork(id: String): MovieDetail
    suspend fun refreshDetail(id : String)
    fun isDatabaseEmpty() : Flow<Boolean>
    fun isDetailEmpty(id: String) : Flow<Boolean>
    suspend fun searchMovies(keyword : String): List<Movie>
}