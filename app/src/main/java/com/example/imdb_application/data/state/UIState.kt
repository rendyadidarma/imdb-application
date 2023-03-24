package com.example.imdb_application.data.state

import com.example.imdb_application.data.sealed.StateLoad

data class UIState<T>(
    var state : StateLoad,
    var value : T?
)