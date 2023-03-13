package com.example.imdb_application.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.remote.api.APINetwork
import com.example.imdb_application.data.remote.dto.MovieDto
import com.example.imdb_application.data.remote.dto.MovieDtoSearch
import com.example.imdb_application.data.remote.dto.ResultsMovieContainer
import com.example.imdb_application.data.utils.MovieObjectMapper
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(private val database: MovieDatabase) : MovieRepository {

    override fun getMovieFromDatabase() : LiveData<List<Movie>> {
        return Transformations.map(database.movieDao.getMovies().asLiveData()) {
            MovieObjectMapper.mapMovieEntityListToMovieList(it)
        }
    }

    override fun getMovieDetail(id : String) : Flow<MovieEntity> {
        return database.movieDao.getMovie(id)
    }

    override suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            val moviesList = APINetwork.movies.getMovieInTheaters()
            database.movieDao.insertAll(MovieObjectMapper.mapMovieDtoToMovieEntity(moviesList.movies))
        }
    }

}