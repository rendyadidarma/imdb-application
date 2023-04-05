package com.example.imdb_application.data.repository

import android.content.Context
import com.example.imdb_application.data.local.database.DetailEntity
import com.example.imdb_application.data.local.database.MovieDao
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.remote.api.APIService
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.NetworkResponseWrapper
import com.example.imdb_application.data.utils.MovieObjectMapper
import com.example.imdb_application.data.utils.NetworkChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val databaseDao: MovieDao,
    private val network: APIService,
    @ApplicationContext private val applicationContext: Context
) : MovieRepository {

    //Inet -> Ambil Data Network -> Update DB -> Tampilin Data dari Network
    //No Inet -> Check DB [DB Kosong -> Tampilin Empty State] [DB Berisi -> Tampilin Data dari DB]
    override suspend fun getMovies(isDatabaseEmpty: Boolean): Flow<NetworkResponseWrapper<List<Movie>>> {
        val networkUnavailableAndDataNotEmpty =
            NetworkChecker.isOnline(applicationContext).not() && isDatabaseEmpty.not()
        val networkUnavailableAndDataEmpty =
            NetworkChecker.isOnline(applicationContext).not() && isDatabaseEmpty

        when {
            networkUnavailableAndDataNotEmpty -> {
                return databaseDao.getMovies().map {
                    NetworkResponseWrapper(
                        StateOnline.NetworkUnavailable,
                        MovieObjectMapper.mapMovieEntityListToMovieList(it)
                    )
                }
            }

            networkUnavailableAndDataEmpty -> {
                return flow {
                    emit(NetworkResponseWrapper(StateOnline.NetworkUnavailable, null))
                }
            }

            else -> {
                val movieListFromNetwork = network.getMovieInTheaters()
                databaseDao.insertAll(
                    MovieObjectMapper.mapMovieDtoToMovieEntity(
                        movieListFromNetwork.movies
                    )
                )
                return MovieObjectMapper.mapMovieDtoListToFlowMovieList(
                    movieListFromNetwork.movies,
                    StateOnline.NetworkAvailable
                )
            }
        }
    }

    override suspend fun searchMovies(keyword: String): Flow<NetworkResponseWrapper<List<Movie>>> {
        return if (NetworkChecker.isOnline(applicationContext)) {
            flow {
                emit(
                    NetworkResponseWrapper(
                        StateOnline.NetworkAvailable,
                        MovieObjectMapper.mapMovieDtoSearchToMovie(network.searchMovie(keyword).moviesForSearch)
                    )
                )
            }
        } else {
            flow {
                emit(NetworkResponseWrapper(StateOnline.NetworkUnavailable, null))
            }
        }
    }

    override fun isDatabaseEmpty(): Flow<Boolean> {
        return databaseDao.isMovieEmpty()
    }

    override fun isDetailEmpty(id: String): Flow<Boolean> {
        return databaseDao.isDetailEmpty(id)
    }

    override suspend fun getDetailNotReturnNetworkState(
        isDetailEmpty: Boolean,
        id: String
    ): Flow<MovieDetail?> {

        val networkIsAvailableAndDataNotFound =
            NetworkChecker.isOnline(applicationContext) && isDetailEmpty
        val networkIsUnavailableAndDataNotFound =
            !NetworkChecker.isOnline(applicationContext) && isDetailEmpty

        when {
            networkIsAvailableAndDataNotFound -> {
                val detail = network.getDetail(id)

                val movieDetailFromNetwork =
                    MovieObjectMapper.mapDetailDtoToMovieDetail(detail)

                databaseDao.insertDetail(MovieObjectMapper.mapDetailDtoToDetailEntity(detail))

                return MovieObjectMapper.mapDetailAsFlow(movieDetailFromNetwork)
            }

            networkIsUnavailableAndDataNotFound -> {
                return MovieObjectMapper.mapDetailAsFlow(movieDetail = null)
            }

            else -> {
                return databaseDao.getDetail(id).map {
                    val movieDetail = MovieObjectMapper.mapDetailEntityToMovieDetail(it)
                    movieDetail
                }
            }

        }
    }

    override fun getAllDetailData() : Observable<List<DetailEntity>> {
        return databaseDao.getAllDetail()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}

//override suspend fun getDetail(
//    isDetailEmpty: Boolean,
//    id: String
//): Flow<NetworkResponseWrapper<MovieDetail>> {
//
//    val networkIsAvailableAndDataNotFound =
//        NetworkChecker.isOnline(applicationContext) && isDetailEmpty
//    val networkIsUnavailableAndDataNotFound =
//        !NetworkChecker.isOnline(applicationContext) && isDetailEmpty
//
//    when {
//        networkIsAvailableAndDataNotFound -> {
//            val detail = network.getDetail(id)
//
//            val movieDetailFromNetwork =
//                MovieObjectMapper.mapDetailDtoToMovieDetail(detail)
//
//            withContext(Dispatchers.IO) {
//                databaseDao.insertDetail(MovieObjectMapper.mapDetailDtoToDetailEntity(detail))
//            }
//
//            return MovieObjectMapper.mapNetworkResponseWrapperDetailAsFlow(
//                NetworkResponseWrapper(StateOnline.NetworkAvailable, movieDetailFromNetwork)
//            )
//        }
//
//        networkIsUnavailableAndDataNotFound -> {
//            return MovieObjectMapper.mapNetworkResponseWrapperDetailAsFlow(
//                NetworkResponseWrapper(StateOnline.NetworkUnavailable, null)
//            )
//        }
//
//        else -> {
//            return databaseDao.getDetail(id).map {
//                val movieDetail = MovieObjectMapper.mapDetailEntityToMovieDetail(it)
//                val responseWrapper =
//                    NetworkResponseWrapper(StateOnline.NetworkAvailable, movieDetail)
//                responseWrapper
//            }
//        }
//
//    }
//}