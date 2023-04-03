package com.example.imdb_application.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemsMovieContainer(@Json(name = "items") val movies: List<MovieDto>)

@JsonClass(generateAdapter = true)
data class ResultsMovieContainer(@Json(name = "results") val moviesForSearch: List<MovieDtoSearch>)

@JsonClass(generateAdapter = true)
data class MovieDtoSearch(
    val id: String,
    val resultType: String,
    val image : String,
    val title : String,
    val description : String
)

@JsonClass(generateAdapter = true)
data class MovieDto(
    val contentRating: String = "",
    val directorList: List<Director> = listOf(),
    val directors: String = "",
    val fullTitle: String = "",
    val genreList: List<Genre> = listOf(),
    val genres: String = "",
    val id: String = "",
    val imDbRating: String = "",
    val imDbRatingCount: String = "",
    val image: String = "",
    val metacriticRating: String = "",
    val plot: String = "",
    val releaseState: String = "",
    val runtimeMins: String = "",
    val runtimeStr: String = "",
    val starList: List<Star> = listOf(),
    val stars: String = "",
    val title: String = "",
    val year: String = ""
)
