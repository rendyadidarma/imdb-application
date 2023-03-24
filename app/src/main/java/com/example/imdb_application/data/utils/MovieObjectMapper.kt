package com.example.imdb_application.data.utils

import com.example.imdb_application.data.local.database.DetailEntity
import com.example.imdb_application.data.local.database.MovieEntity
import com.example.imdb_application.data.model.Movie
import com.example.imdb_application.data.model.MovieDetail
import com.example.imdb_application.data.remote.dto.MovieDto
import com.example.imdb_application.data.remote.dto.MovieDtoSearch
import com.example.imdb_application.data.remote.dto.detail.DetailDto
import com.example.imdb_application.data.sealed.StateOnline
import com.example.imdb_application.data.state.NetworkResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object MovieObjectMapper {
    // from Database Model to Domain Model
    fun mapMovieEntityListToMovieList(movieEntities: List<MovieEntity>): List<Movie> {
        return movieEntities.map {
            Movie(
                fullTitle = it.fullTitle!!,
                id = it.id,
                image = it.image!!
            )
        }
    }

    fun mapDetailEntityToMovieDetail(detailEntity: DetailEntity): MovieDetail {
        return MovieDetail(
            contentRating = detailEntity.contentRating,
            directors = detailEntity.directors,
            fullTitle = detailEntity.fullTitle,
            genres = detailEntity.genres,
            id = detailEntity.id,
            imDbRating = detailEntity.imDbRating,
            imDbRatingCount = detailEntity.imDbRatingCount,
            image = detailEntity.image,
            plot = detailEntity.plot,
            releaseState = detailEntity.releaseState,
            runtimeStr = detailEntity.runtimeStr,
            stars = detailEntity.stars,
            title = detailEntity.title
        )
    }

    fun mapDetailDtoToDetailEntity(detailDto: DetailDto): DetailEntity {
        return DetailEntity(
            contentRating = detailDto.contentRating,
            directors = detailDto.directors,
            fullTitle = detailDto.fullTitle,
            genres = detailDto.genres,
            id = detailDto.id!!,
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

    fun mapDetailDtoToMovieDetail(detailDto: DetailDto): MovieDetail {
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

    fun mapMovieDtoToMovieEntity(movieDtoList: List<MovieDto>): List<MovieEntity> {
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

    fun mapMovieDtoSearchToMovie(movieDtoSearch: List<MovieDtoSearch>): List<Movie> {
        return movieDtoSearch.map {
            Movie(
                fullTitle = it.title,
                id = it.id,
                image = it.image
            )
        }
    }

    fun mapMovieDtoListToFlowMovieList(movieDto: List<MovieDto>, state: StateOnline): Flow<NetworkResponseWrapper<List<Movie>>> {
        return flow {
            emit(
                NetworkResponseWrapper(
                    state,
                    movieDto.map {
                        Movie(
                            fullTitle = it.title,
                            id = it.id,
                            image = it.image
                        )
                    }
                )
            )
        }
    }

    fun mapNetworkResponseWrapperDetailAsFlow(networkResponseWrapper: NetworkResponseWrapper<MovieDetail>): Flow<NetworkResponseWrapper<MovieDetail>> {
        return flow {
            emit(
                NetworkResponseWrapper(
                    isInternetAvailable = networkResponseWrapper.isInternetAvailable,
                    value = networkResponseWrapper.value
                )
            )
        }
    }

    fun mapMovieDtoListToMovieList(movieDtoSearch: List<MovieDto>): List<Movie> {
        return movieDtoSearch.map {
            Movie(
                fullTitle = it.title,
                id = it.id,
                image = it.image
            )
        }
    }

}