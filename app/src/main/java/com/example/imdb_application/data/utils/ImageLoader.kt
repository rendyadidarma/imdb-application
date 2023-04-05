package com.example.imdb_application.data.utils

import android.content.Context
import android.widget.ImageView
import coil.load
import coil.request.CachePolicy
import com.example.imdb_application.R

object ImageLoader {

    fun loadImage(
        context: Context,
        url: String?,
        view: ImageView
    ) {
        val imgLoader = coil.ImageLoader.Builder(context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .error(R.drawable.ic_broken_image)
            .build()

        view.load(url, imgLoader)
    }
}