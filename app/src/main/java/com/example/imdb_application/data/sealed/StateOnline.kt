package com.example.imdb_application.data.sealed

sealed class StateOnline {
    object NetworkAvailable : StateOnline()
    object NetworkUnavailable : StateOnline()
}