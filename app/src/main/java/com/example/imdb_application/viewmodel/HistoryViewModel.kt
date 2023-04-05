package com.example.imdb_application.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.local.database.DetailEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.data.utils.MovieObjectMapper
import com.example.imdb_application.data.utils.network24.ConnectivityObserver
import com.example.imdb_application.data.utils.network24.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: MovieRepository, @ApplicationContext private val applicationContext: Context) : ViewModel() {
// solution about params warning https://stackoverflow.com/questions/66216839/inject-context-with-hilt-this-field-leaks-a-context-object
    private var _detailList = MutableLiveData<List<Movie>?>()
    val detailList : LiveData<List<Movie>?> get() = _detailList

    private lateinit var connectivityObserver: ConnectivityObserver

    fun observeConnectivity() {
        connectivityObserver = NetworkObserver(applicationContext)
        connectivityObserver.observeState().onEach {
            when(it) {
                ConnectivityObserver.Status.Available -> {
                    Log.d("NetworkStatus", "Available")
                }
                ConnectivityObserver.Status.Unavailable -> {
                    Log.d("NetworkStatus", "Unavailable")
                }
                ConnectivityObserver.Status.Losing -> {
                    Log.d("NetworkStatus", "Losing")
                }
                ConnectivityObserver.Status.Lost -> {
                    Log.d("NetworkStatus", "Lost")
                }
            }
        }.launchIn(viewModelScope)

    }

    private fun getListObserver(): Observer<List<DetailEntity>> {
        return object : Observer<List<DetailEntity>> {
            override fun onSubscribe(d: Disposable) {
                // showing progress indicator
            }

            override fun onError(e: Throwable) {
                _detailList.value = null
            }

            override fun onComplete() {
                // hide progress indicator
            }

            override fun onNext(t: List<DetailEntity>) {
                _detailList.value = MovieObjectMapper.mapDetailEntityListToMovieList(t)
            }

        }
    }

    fun getAllDetail() {
        repository.getAllDetailData()
            .subscribe(getListObserver())
    }

}