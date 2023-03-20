package com.example.imdb_application.view.adapter

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.imdb_application.R
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.viewmodel.STATUS
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.flow.StateFlow
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
                shimmerFrameLayout.startShimmer()
            }
        }
    }
    if (onLoadSearch != null) {
        when (onLoadSearch.value) {
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

@BindingAdapter(value = ["noDataStatus", "anotherView"], requireAll = false)
fun bindImageBasedOnStatus(view : ImageView, status : Boolean, anotherView: View?) {
    if(status) {
        view.visibility = View.VISIBLE
        anotherView?.visibility = View.GONE
    } else {
        view.visibility = View.GONE
        anotherView?.visibility = View.VISIBLE
    }
}

// TODO : Change This Binding Adapter Function to More General (move to onKeywordChange.kt)
//@BindingAdapter("textChangeListener")
//fun textChangeListener(
//    view: EditText,
//    debounceTime : Long = 1000,
//    callback: (keyword : String) -> Unit
//) {
//
//    var job: Job? = null
//
//    view.addTextChangedListener(object : TextWatcher {
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//        }
//
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            job?.cancel()
//        }
//
//        override fun afterTextChanged(p0: Editable?) {
//            if (!p0.isNullOrEmpty()) {
//                val input = p0.toString()
//                job = callback(input)
//            }
//        }
//
//    })
//}


