package com.example.imdb_application.data.sealed

sealed class StateLoad {
    object Loading : StateLoad()
    object Success : StateLoad()
    object Error : StateLoad()

}