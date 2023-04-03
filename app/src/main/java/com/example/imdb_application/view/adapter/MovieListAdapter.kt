package com.example.imdb_application.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.utils.ImageLoader
import com.example.imdb_application.databinding.ListViewItemBinding

class MovieListAdapter(private val clickListener: MovieListener) : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(DiffCallback){
    companion object DiffCallback : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.image == newItem.image && oldItem.fullTitle == newItem.fullTitle && oldItem.id == newItem.id
        }
    }

    class MovieViewHolder(private var binding: ListViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (movieListener: MovieListener,Movie: Movie) {
            binding.movieTitle.text = Movie.fullTitle
            ImageLoader.loadImage(binding.root.context, Movie.image, binding.movieImage)
            binding.itemCard.setOnClickListener {movieListener.onClick(Movie) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val binding = ListViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val itemMovie = getItem(position)
        holder.bind(clickListener, itemMovie)
    }
}

class MovieListener(val clickListener: (movie: Movie) -> Unit) {
    fun onClick(movie: Movie) = clickListener(movie)
}
