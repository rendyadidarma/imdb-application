package com.example.imdb_application.data.utils

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.imdb_application.R
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.view.adapter.MovieListAdapter
import com.example.imdb_application.viewmodel.STATUS
import com.facebook.shimmer.ShimmerFrameLayout

object BindUtils {

    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                error(R.drawable.ic_broken_image)
            }
        }
    }

    fun bindRecyclerView(
        recyclerView: RecyclerView,
        data: List<Movie>?
    ) {
        val adapter = recyclerView.adapter as MovieListAdapter
        adapter.submitList(data)
    }



    fun bindShimmer(shimmerFrameLayout: ShimmerFrameLayout, onLoadSearch: STATUS? = null, recyclerViews: RecyclerView? = null,
                    imageViews : ImageView? = null) {
        if (onLoadSearch != null) {
            when (onLoadSearch) {
                STATUS.ON_LOAD -> {
                    recyclerViews?.visibility = View.GONE
                    imageViews?.visibility = View.GONE
                    shimmerFrameLayout.visibility = View.VISIBLE
                    shimmerFrameLayout.startShimmer()
                }

                STATUS.NO_DATA -> {
                    imageViews?.visibility = View.VISIBLE
                    recyclerViews?.visibility = View.GONE
                    shimmerFrameLayout.visibility = View.GONE
                    shimmerFrameLayout.stopShimmer()
                }

                else -> {
                    recyclerViews?.visibility = View.VISIBLE
                    imageViews?.visibility = View.GONE
                    shimmerFrameLayout.visibility = View.GONE
                    shimmerFrameLayout.stopShimmer()
                }

            }
        }

    }

}