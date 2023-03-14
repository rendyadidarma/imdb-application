package com.example.imdb_application.data.remote.dto.detail

data class BoxOffice(
    val budget: String,
    val cumulativeWorldwideGross: String,
    val grossUSA: String,
    val openingWeekendUSA: String
)