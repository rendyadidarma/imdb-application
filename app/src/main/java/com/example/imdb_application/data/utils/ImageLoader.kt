package com.example.imdb_application.data.utils

import android.widget.ImageView
import androidx.core.net.toUri
import coil.load
import com.example.imdb_application.R

object ImageLoader {

    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                error(R.drawable.ic_broken_image)
            }
        }
    }

}