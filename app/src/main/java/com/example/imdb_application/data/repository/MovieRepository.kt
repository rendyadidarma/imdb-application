package com.example.imdb_application.data.repository

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

    fun getMovieFromDatabase() : LiveData<List<Movie>>

    fun getMovieDetail(id : String) : Flow<MovieEntity>

    suspend fun getDetailFromNetwork(id : String) : MovieDetail
    suspend fun refreshMovies()

//    suspend fun getMovieFromNetwork(keyword : String) : List<MovieDtoSearch>
}