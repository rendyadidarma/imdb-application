package com.example.imdb_application.data.state

import com.example.imdb_application.data.sealed.StateOnline

data class NetworkResponseWrapper<T>(
    val isInternetAvailable : StateOnline,
    val value : T?
)
