package com.example.imdb_application.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.example.imdb_application.data.local.database.MovieDatabase
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.remote.api.APIService
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
    @WorkerThread
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
        return database.movieDao.isMovieEmpty()
    }

    override fun isDetailEmpty(id: String) : Flow<Boolean> {
        return database.movieDao.isDetailEmpty(id)
    }

    // From Database
    override suspend fun getDetailFromDatabase(id : String) : Flow<MovieDetail>? {
        val isDetailNotFound = database.movieDao.isDetailEmpty(id).first()
        if(isDetailNotFound) {
           refreshDetail(id)
        } else {
            return database.movieDao.getDetail(id).map { MovieObjectMapper.mapDetailEntityToMovieDetail(it) }
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
            database.movieDao.insertDetail(MovieObjectMapper.mapDetailDtoToDetailEntity(detail))
        }
    }

    @WorkerThread
    override suspend fun getMoviesFromNetwork() : List<Movie> {
        return MovieObjectMapper.mapMovieDtoListToMovieList(network.getMovieInTheaters().movies)
    }



}