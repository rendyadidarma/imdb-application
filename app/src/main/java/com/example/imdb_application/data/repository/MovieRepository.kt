package com.example.imdb_application.data.repository

import com.example.imdb_application.data.local.database.DetailEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.state.NetworkResponseWrapper
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(isDatabaseEmpty: Boolean): Flow<NetworkResponseWrapper<List<Movie>>>
    suspend fun getDetailNotReturnNetworkState(
        isDetailEmpty: Boolean,
        id: String
    ): Flow<MovieDetail?>

    fun isDetailEmpty(id: String): Flow<Boolean>
    fun isDatabaseEmpty(): Flow<Boolean>
    suspend fun searchMovies(keyword: String): Flow<NetworkResponseWrapper<List<Movie>>>
    fun getAllDetailData() : Observable<List<DetailEntity>>
}

//suspend fun getDetail(
//    isDetailEmpty: Boolean,
//    id: String
//): Flow<NetworkResponseWrapper<MovieDetail>>