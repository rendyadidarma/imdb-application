package com.example.imdb_application.data.utils.network24

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observeState() : Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }

}