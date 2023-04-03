package com.example.imdb_application.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("movie_table")
data class MovieEntity constructor(
    val contentRating: String? = null,
    val directors: String? = null,
    val fullTitle: String? = null,
    val genres: String? = null,
    @PrimaryKey
    val id: String = "",
    val imDbRating: String? = null,
    val imDbRatingCount: String? = null,
    val image: String? = null,
    val plot: String? = null,
    val releaseState: String? = null,
    val runtimeStr: String? = null,
    val stars: String? = null,
    val title: String? = null,
    val year: String? = null
)

@Entity("movie_detail")
data class DetailEntity constructor(
    val contentRating: String? = null,
    val directors: String? = null,
    val fullTitle: String? = null,
    val genres: String? = null,
    @PrimaryKey val id: String = "",
    val imDbRating: String? = null,
    val imDbRatingCount: String? = null,
    val image: String? = null,
    val plot: String? = null,
    val releaseState: String? = null,
    val runtimeStr: String? = null,
    val stars: String? = null,
    val title: String? = null
)
