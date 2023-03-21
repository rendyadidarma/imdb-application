package com.example.imdb_application.data.repository

import android.content.Context
import com.example.imdb_application.data.local.database.MovieDao
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.remote.api.APIService
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.NetworkResponseWrapper
import com.example.imdb_application.data.utils.MovieObjectMapper
import com.example.imdb_application.data.utils.NetworkChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val databaseDao: MovieDao,
    private val network: APIService,
    @ApplicationContext private val applicationContext: Context
) : MovieRepository {

    //Inet -> Ambil Data -> Update DB -> Tampilin Data
    //No Inet -> Check DB [DB Kosong -> Tampilin Empty State] [DB Berisi -> Tampilin Data]
    override suspend fun getMovies(): Flow<List<Movie>>? {
        // 1
        if (NetworkChecker.isOnline(applicationContext)) {
            val moviesList = network.getMovieInTheaters()
            withContext(Dispatchers.IO) {
                databaseDao.insertAll(MovieObjectMapper.mapMovieDtoToMovieEntity(moviesList.movies))
            }
            return MovieObjectMapper.mapMovieDtoListToFlowMovieList(moviesList.movies)
        } else if (!NetworkChecker.isOnline(applicationContext)) { // 2
            val emptyDb = isDatabaseEmpty().first()
            if (emptyDb) {
                return null
            }
        }

        return databaseDao.getMovies().map { MovieObjectMapper.mapMovieEntityListToMovieList(it) }
    }

    override suspend fun searchMovies(keyword: String): List<Movie> {
        return MovieObjectMapper.mapMovieDtoSearchToMovie(network.searchMovie(keyword).moviesForSearch)
    }

    override fun isDatabaseEmpty(): Flow<Boolean> {
        return databaseDao.isMovieEmpty()
    }

    override fun isDetailEmpty(id: String): Flow<Boolean> {
        return databaseDao.isDetailEmpty(id)
    }

    // From Database
    override suspend fun getDetail(isDetailEmpty : Boolean, id: String): Flow<NetworkResponseWrapper<MovieDetail>> {

        val networkIsAvailableAndDataNotFound =
            NetworkChecker.isOnline(applicationContext) && isDetailEmpty
        val networkIsUnavailableAndDataNotFound =
            !NetworkChecker.isOnline(applicationContext) && isDetailEmpty

        when {
            networkIsAvailableAndDataNotFound -> {
                val movieDetailFromNetwork =
                    MovieObjectMapper.mapDetailDtoToMovieDetail(network.getDetail(id))
                return MovieObjectMapper.mapNetworkResponseWrapperDetailAsFlow(
                    NetworkResponseWrapper(StateOnline.networkAvailable, movieDetailFromNetwork)
                )
            }

            networkIsUnavailableAndDataNotFound -> {
                return MovieObjectMapper.mapNetworkResponseWrapperDetailAsFlow(
                    NetworkResponseWrapper(StateOnline.networkUnavailable, null)
                )
            }

            else -> {
                return databaseDao.getDetail(id).mapLatest {
                    val movieDetail = MovieObjectMapper.mapDetailEntityToMovieDetail(it)
                    val responseWrapper =
                        NetworkResponseWrapper(StateOnline.networkAvailable, movieDetail)
                    responseWrapper
                }
            }

        }
    }
    // TODO : difference between flatMapLatest and flatMapMerge

    override suspend fun refreshDetail(id: String) {
        withContext(Dispatchers.IO) {
            val detail = network.getDetail(id)
            databaseDao.insertDetail(MovieObjectMapper.mapDetailDtoToDetailEntity(detail))
        }
    }

    override suspend fun getMoviesFromNetwork(): List<Movie> {
        return MovieObjectMapper.mapMovieDtoListToMovieList(network.getMovieInTheaters().movies)
    }

}