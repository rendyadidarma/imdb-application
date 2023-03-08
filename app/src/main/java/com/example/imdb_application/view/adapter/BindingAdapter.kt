package com.example.imdb_application.view.adapter

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.imdb_application.R
import com.example.imdb_application.data.model.Movie
import com.facebook.shimmer.ShimmerFrameLayout

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri){
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listOfData")
fun bindRecyclerView(recyclerView: RecyclerView,
                     data: List<Movie>?) {
    val adapter = recyclerView.adapter as MovieListAdapter
    adapter.submitList(data)
}

@BindingAdapter("shimmerStatus")
fun bindShimmer(shimmerFrameLayout: ShimmerFrameLayout, hasData: Boolean) {
    when(hasData) {
        true -> {
            shimmerFrameLayout.visibility = View.GONE
            shimmerFrameLayout.stopShimmer()
        }
        false -> {
            shimmerFrameLayout.visibility = View.VISIBLE
            shimmerFrameLayout.startShimmer();
        }
    }
}

