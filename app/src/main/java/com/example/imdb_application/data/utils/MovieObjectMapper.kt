package com.example.imdb_application.data.utils

import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.remote.dto.MovieDto
import com.example.imdb_application.data.remote.dto.MovieDtoSearch
import com.example.imdb_application.data.remote.dto.detail.DetailDto

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

    fun mapMovieEntityToMovieDetail(movieEntity: MovieEntity) : MovieDetail {
        return MovieDetail(
            contentRating = movieEntity.contentRating,
            directors = movieEntity.directors,
            fullTitle = movieEntity.fullTitle,
            genres = movieEntity.genres,
            id = movieEntity.id,
            imDbRating = movieEntity.imDbRating,
            imDbRatingCount = movieEntity.imDbRatingCount,
            image = movieEntity.image,
            plot = movieEntity.plot,
            releaseState = movieEntity.releaseState,
            runtimeStr = movieEntity.runtimeStr,
            stars = movieEntity.stars,
            title = movieEntity.title
        )
    }

    fun mapDetailDtoToMovieDetail(detailDto: DetailDto) : MovieDetail {
        return MovieDetail(
            contentRating = detailDto.contentRating,
            directors = detailDto.directors,
            fullTitle = detailDto.fullTitle,
            genres = detailDto.genres,
            id = detailDto.id,
            imDbRating = detailDto.imDbRating,
            imDbRatingCount = detailDto.imDbRatingVotes,
            image = detailDto.image,
            plot = detailDto.plot,
            releaseState = detailDto.releaseDate,
            runtimeStr = detailDto.runtimeStr,
            stars = detailDto.stars,
            title = detailDto.title
        )
    }
    fun mapMovieDtoToMovieDetail(movieDto: MovieDto) : MovieDetail {
        return MovieDetail(
            contentRating = movieDto.contentRating,
            directors = movieDto.directors,
            fullTitle = movieDto.fullTitle,
            genres = movieDto.genres,
            id = movieDto.id,
            imDbRating = movieDto.imDbRating,
            imDbRatingCount = movieDto.imDbRatingCount,
            image = movieDto.image,
            plot = movieDto.plot,
            releaseState = movieDto.releaseState,
            runtimeStr = movieDto.runtimeStr,
            stars = movieDto.stars,
            title = movieDto.title
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

    fun mapMovieDtoSearchToMovie(movieDtoSearch: List<MovieDtoSearch>) : List<Movie> {
        return movieDtoSearch.map {
            Movie (
            fullTitle = it.title,
            id = it.id,
            image = it.image
            )
        }
    }

}