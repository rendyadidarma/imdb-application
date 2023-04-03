package com.example.imdb_application.data.utils

import androidx.navigation.NavController
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.view.fragments.HomeFragmentDirections
import com.example.imdb_application.view.fragments.SearchFragmentDirections

object Router {
    fun routeHomeFragmentToDetailFragment(movie : Movie, navController: NavController) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToDetailFragment().setMovieId(movie.id)
        navController.navigate(action)
    }

    fun routeSearchFragmentToDetailFragment(movie: Movie, navController: NavController) {
        val action =
            SearchFragmentDirections.actionSearchFragmentToDetailFragment().setMovieId(movie.id)
        navController.navigate(action)
    }

}
