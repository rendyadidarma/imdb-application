package com.example.imdb_application.data.model

import com.example.imdb_application.data.remote.dto.Director
import com.example.imdb_application.data.remote.dto.Genre
import com.example.imdb_application.data.remote.dto.Star

data class Movie(
    val fullTitle: String,
    val id: String,
    val image: String
)
