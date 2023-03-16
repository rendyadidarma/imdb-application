package com.example.imdb_application.data.repository

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.Transformations
import com.example.imdb_application.data.local.database.MovieDao
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.remote.api.APIService
import com.example.imdb_application.data.remote.dto.MovieDtoSearch
import com.example.imdb_application.data.utils.MovieObjectMapper
import com.example.imdb_application.data.utils.NetworkChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val databaseDao: MovieDao,
    private val network: APIService,
    private val context: Context) : MovieRepository {

    //TODO: Inet -> Ambil Data -> Update DB -> Tampilin Data
    //TODO: No Inet -> Check DB [DB Kosong -> Tampilin Empty State] [DB Berisi -> Tampilin Data]
    @WorkerThread
    override suspend fun getMovies(): Flow<List<Movie>>? {
        // 1
        if(NetworkChecker.isOnline(context.applicationContext)) {
            withContext(Dispatchers.IO) {
                val  moviesList = network.getMovieInTheaters()
                databaseDao.insertAll(MovieObjectMapper.mapMovieDtoToMovieEntity(moviesList.movies))
            }
        } else if (!NetworkChecker.isOnline(context.applicationContext)) { // 2
            val emptyDb = isDatabaseEmpty().first()
            if(emptyDb) {
                return null
            }
        }

        return databaseDao.getMovies().map { MovieObjectMapper.mapMovieEntityListToMovieList(it) }
    }

    @WorkerThread
    override suspend fun searchMovies(keyword : String): List<Movie> {
        return MovieObjectMapper.mapMovieDtoSearchToMovie(network.searchMovie(keyword).moviesForSearch)
    }

    @WorkerThread
    override fun isDatabaseEmpty() : Flow<Boolean> {
        return databaseDao.isMovieEmpty()
    }

    @WorkerThread
    override fun isDetailEmpty(id: String) : Flow<Boolean> {
        return databaseDao.isDetailEmpty(id)
    }

    // From Database
    @WorkerThread
    override suspend fun getDetailFromDatabase(id : String) : Flow<MovieDetail>? {
        val isDetailNotFound = databaseDao.isDetailEmpty(id).first()
        if(isDetailNotFound) {
           refreshDetail(id)
        } else {
            return databaseDao.getDetail(id).map { MovieObjectMapper.mapDetailEntityToMovieDetail(it) }
        }

        return null
    }

    @WorkerThread
    override suspend fun getDetailFromNetwork(id: String): MovieDetail {
        return MovieObjectMapper.mapDetailDtoToMovieDetail(network.getDetail(id))
    }
    @WorkerThread
    override suspend fun refreshDetail(id : String) {
        withContext(Dispatchers.IO) {
            val detail = network.getDetail(id)
            databaseDao.insertDetail(MovieObjectMapper.mapDetailDtoToDetailEntity(detail))
        }
    }

    @WorkerThread
    override suspend fun getMoviesFromNetwork() : List<Movie> {
        return MovieObjectMapper.mapMovieDtoListToMovieList(network.getMovieInTheaters().movies)
    }

}