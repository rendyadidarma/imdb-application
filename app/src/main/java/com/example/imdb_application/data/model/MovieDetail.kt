package com.example.imdb_application.data.model

import androidx.room.PrimaryKey

data class MovieDetail(
    val contentRating: String?,
    val directors: String?,
    val fullTitle: String?,
    val genres: String?,
    val id: String,
    val imDbRating: String?,
    val imDbRatingCount: String?,
    val image: String?,
    val plot: String?,
    val releaseState: String?,
    val runtimeStr: String?,
    val stars: String?,
    val title: String?
)
