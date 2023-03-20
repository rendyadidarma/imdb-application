package com.example.imdb_application.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("movie_table")
data class MovieEntity constructor(
    val contentRating: String?,
    val directors: String?,
    val fullTitle: String?,
    val genres: String?,
    @PrimaryKey
    val id: String,
    val imDbRating: String?,
    val imDbRatingCount: String?,
    val image: String?,
    val plot: String?,
    val releaseState: String?,
    val runtimeStr: String?,
    val stars: String?,
    val title: String?,
    val year: String?
)

@Entity("movie_detail")
data class DetailEntity constructor(
    val contentRating: String?,
    val directors: String?,
    val fullTitle: String?,
    val genres: String?,
    @PrimaryKey
    val id: String = "",
    val imDbRating: String?,
    val imDbRatingCount: String?,
    val image: String?,
    val plot: String?,
    val releaseState: String?,
    val runtimeStr: String?,
    val stars: String?,
    val title: String?
)
