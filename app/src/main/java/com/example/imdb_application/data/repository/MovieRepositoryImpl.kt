package com.example.imdb_application.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
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
import com.example.imdb_application.data.utils.NetworkChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(private val database: MovieDatabase, private val network: APIService) : MovieRepository {


    //TODO: Inet -> Ambil Data -> Update DB -> Tampilin Data
    //TODO: No Inet -> Check DB [DB Kosong -> Tampilin Empty State] [DB Berisi -> Tampilin Data]
    override suspend fun getMovies(context: Context): Flow<List<Movie>>? {
        // 1
        if(NetworkChecker.isOnline(context)) {
            withContext(Dispatchers.IO) {
                val  moviesList = network.getMovieInTheaters()
                database.movieDao.insertAll(MovieObjectMapper.mapMovieDtoToMovieEntity(moviesList.movies))
            }
        } else if (!NetworkChecker.isOnline(context)) { // 2
            val emptyDb = isDatabaseEmpty().first()
            if(emptyDb) {
                return null
            }
        }

        return database.movieDao.getMovies().map { MovieObjectMapper.mapMovieEntityListToMovieList(it) }
    }

    override fun isDatabaseEmpty() : Flow<Boolean> {
        return database.movieDao.isTableEmpty()
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

    override suspend fun getMoviesFromNetwork() : List<Movie> {
        return MovieObjectMapper.mapMovieDtoListToMovieList(network.getMovieInTheaters().movies)
    }



}