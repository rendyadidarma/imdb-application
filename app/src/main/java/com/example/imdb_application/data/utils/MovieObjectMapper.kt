package com.example.imdb_application.data.utils

import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.remote.dto.MovieDto

object MovieObjectMapper {
    // from Database Model to Domain Model
    fun mapMovieEntityListToMovieList(movieEntities: List<MovieEntity>): List<Movie> {
        return movieEntities.map {
            Movie(
                fullTitle = it.fullTitle,
                id = it.id,
                image = it.image
            )
        }
    }

    fun mapMovieEntityToMovie(movieEntity: MovieEntity): Movie {
        return Movie(
            fullTitle = movieEntity.fullTitle,
            id = movieEntity.id,
            image = movieEntity.image
        )
    }

    fun mapMovieDtoToMovieEntity(movieDtoList : List<MovieDto>) : List<MovieEntity> {
        return movieDtoList.map {
            MovieEntity(
                contentRating = it.contentRating,
                directors = it.directors,
                fullTitle = it.fullTitle,
                genres = it.genres,
                id = it.id,
                imDbRating = it.imDbRating,
                imDbRatingCount = it.imDbRatingCount,
                image = it.image,
                plot = it.plot,
                releaseState = it.releaseState,
                runtimeStr = it.runtimeStr,
                stars = it.stars,
                title = it.title,
                year = it.year
            )
        }
    }

}