package com.example.imdb_application.data.remote.api

import com.example.imdb_application.data.remote.dto.ItemsMovieContainer
import com.example.imdb_application.data.remote.dto.MovieDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

interface APIService {

    @GET("/API/InTheaters/${com.example.imdb_application.BuildConfig.API_KEY}")
    suspend fun getMovieInTheaters() : ItemsMovieContainer

//    @GET("/API/Search/${BuildConfig.API_KEY}/{keyword}")
//    suspend fun searchMovie(@Path("keyword") keyword: String)
}

object APINetwork {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://imdb-api.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val movies = retrofit.create(APIService::class.java)
}