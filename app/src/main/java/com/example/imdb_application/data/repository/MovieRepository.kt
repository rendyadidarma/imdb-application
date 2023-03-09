package com.example.imdb_application.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.remote.api.APINetwork
import com.example.imdb_application.data.utils.MovieObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepository(private val database: MovieDatabase) {

    val movies : LiveData<List<Movie>> = Transformations.map(database.movieDao.getMovies().asLiveData()) {
        MovieObjectMapper.mapMovieEntityListToMovieList(it)
    }

    fun getMovieDetail(id : String) : Flow<Movie> {
        return database.movieDao.getMovie(id).map { MovieObjectMapper.mapMovieEntityToMovie(it) }
    }

    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            val moviesList = APINetwork.movies.getMovieInTheaters()
            database.movieDao.insertAll(MovieObjectMapper.mapMovieDtoToMovieEntity(moviesList.movies))
        }
    }

}