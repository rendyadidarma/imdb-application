package com.example.imdb_application.data.remote.dto

import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.remote.api.APINetwork.movies
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemsMovieContainer(@Json(name = "items") val movies: List<MovieDto>)

@JsonClass(generateAdapter = true)
data class MovieDto(
    val contentRating: String,
    val directorList: List<Director>,
    val directors: String,
    val fullTitle: String,
    val genreList: List<Genre>,
    val genres: String,
    val id: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    val image: String,
    val metacriticRating: String,
    val plot: String,
    val releaseState: String,
    val runtimeMins: String,
    val runtimeStr: String,
    val starList: List<Star>,
    val stars: String,
    val title: String,
    val year: String
)

fun ItemsMovieContainer.asDatabaseModel() : List<MovieEntity> {
    return movies.map {
        MovieEntity(
            contentRating = it.contentRating,
            directors = it.directors,
            fullTitle = it.fullTitle,
            genres = it.genres,
            id = it.id,
            imDbRating = it.imDbRating,
            imDbRatingCount = it.imDbRatingCount,
            image = it.image,
            plot = it.plot,
            releaseState = it.releaseState,
            runtimeStr = it.runtimeStr,
            stars = it.stars,
            title = it.title,
            year = it.year
        )
    }
}

// TODO: Make an Database Model and Convert Dto to DB model
