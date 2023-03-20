package com.example.imdb_application.data.remote.api

import com.example.imdb_application.BuildConfig
import com.example.imdb_application.data.remote.dto.ItemsMovieContainer
import com.example.imdb_application.data.remote.dto.ResultsMovieContainer
import com.example.imdb_application.data.remote.dto.detail.DetailDto
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    @GET("/API/InTheaters/${BuildConfig.API_KEY}")
    suspend fun getMovieInTheaters() : ItemsMovieContainer

    @GET("/API/Search/${BuildConfig.API_KEY}/{keyword}")
    suspend fun searchMovie(@Path("keyword") keyword: String) : ResultsMovieContainer

    // https://imdb-api.com/en/API/Title/k_v50vt488/tt0110413
    @GET("/API/Title/${BuildConfig.API_KEY}/{id}")
    suspend fun getDetail(@Path("id") itemId: String) : DetailDto

}