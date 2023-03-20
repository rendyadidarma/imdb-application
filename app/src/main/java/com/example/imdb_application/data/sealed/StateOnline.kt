package com.example.imdb_application.data.sealed

sealed class StateOnline {
    object networkAvailable : StateOnline()
    object networkUnavailable : StateOnline()
}