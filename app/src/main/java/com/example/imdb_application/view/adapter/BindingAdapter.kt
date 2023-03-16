package com.example.imdb_application.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.Key.VISIBILITY
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.imdb_application.R
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.viewmodel.STATUS
import com.example.imdb_application.viewmodel.SearchViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listOfData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<Movie>?
) {
    val adapter = recyclerView.adapter as MovieListAdapter
    adapter.submitList(data)
}

@BindingAdapter(value = ["shimmerStatus", "onLoadSearch", "recyclerViews", "imageViews", "dbEmpty"], requireAll = false)
fun bindShimmer(shimmerFrameLayout: ShimmerFrameLayout, hasData: Boolean?, onLoadSearch: StateFlow<STATUS>?, recyclerViews: RecyclerView?, imageViews : ImageView?, dbEmpty : Boolean?) {
    if(hasData != null) {
        when (hasData) {
            true -> {
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmer()
                if(dbEmpty != null && dbEmpty == true)
                    imageViews?.visibility = View.VISIBLE
                else
                    imageViews?.visibility = View.GONE
            }
            false -> {
                imageViews?.visibility = View.GONE
                shimmerFrameLayout.visibility = View.VISIBLE
                shimmerFrameLayout.startShimmer();
            }
        }
    }
    if (onLoadSearch != null) {
        when (onLoadSearch.value) {
            STATUS.ONLOAD -> {
                recyclerViews?.visibility = View.GONE
                imageViews?.visibility = View.GONE
                shimmerFrameLayout.visibility = View.VISIBLE
                shimmerFrameLayout.startShimmer()
            }

            STATUS.NODATA -> {
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

// TODO : Change This Binding Adapter Function to More General
@BindingAdapter("textChangeListener")
fun textChangeListener(
    view: EditText,
    callback: (keyword : String) -> Job
) {

    var job: Job? = null

    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            job?.cancel()
        }

        override fun afterTextChanged(p0: Editable?) {
            if (!p0.isNullOrEmpty()) {
                val input = p0.toString()
                job = callback(input)
            }
        }

    })

}


