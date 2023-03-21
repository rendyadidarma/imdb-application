package com.example.imdb_application.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.repository.MovieRepository
import com.example.imdb_application.view.fragments.DetailFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository : MovieRepository
) : ViewModel() {

    private val args = DetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val isDetailEmpty = movieRepository.isDetailEmpty(args.movieId).map { it }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), true
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun movieInDetail(detailEmpty: Boolean): Flow<MovieDetail?> {
        return movieRepository.getDetail(detailEmpty, args.movieId).mapLatest {
            it.value
        }.conflate()
    }

//    val movieInDetail = movieRepository.getDetail(args.movieId).map { it.value }.
//        stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(),
//            null
//        )

}