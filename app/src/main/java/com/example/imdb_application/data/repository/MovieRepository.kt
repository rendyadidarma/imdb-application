package com.example.imdb_application.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.local.database.asDomainModel
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.remote.api.APINetwork
import com.example.imdb_application.data.remote.dto.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val database: MovieDatabase) {

    val movies : LiveData<List<Movie>> = Transformations.map(database.movieDao.getMovies().asLiveData()) {
        it.asDomainModel()
    }

    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            val moviesList = APINetwork.movies.getMovieInTheaters()
            database.movieDao.insertAll(moviesList.asDatabaseModel())
        }
    }

}