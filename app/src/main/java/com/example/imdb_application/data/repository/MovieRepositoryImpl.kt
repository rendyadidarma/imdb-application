package com.example.imdb_application.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.remote.api.APIService
import com.example.imdb_application.data.remote.dto.detail.DetailDto
import com.example.imdb_application.data.utils.MovieObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(private val database: MovieDatabase, private val network: APIService) : MovieRepository {

    override fun getMovieFromDatabase() : LiveData<List<Movie>> {
        return Transformations.map(database.movieDao.getMovies().asLiveData()) {
            MovieObjectMapper.mapMovieEntityListToMovieList(it)
        }
    }

    // From Database
    override fun getMovieDetail(id : String) : Flow<MovieEntity> {
        return database.movieDao.getMovie(id)
    }

    override suspend fun getDetailFromNetwork(id: String): MovieDetail {
        return MovieObjectMapper.mapDetailDtoToMovieDetail(network.getDetail(id))
    }

    override suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            val moviesList = network.getMovieInTheaters()
            database.movieDao.insertAll(MovieObjectMapper.mapMovieDtoToMovieEntity(moviesList.movies))
        }
    }



}