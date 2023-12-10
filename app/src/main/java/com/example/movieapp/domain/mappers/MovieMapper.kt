package com.example.movieapp.domain.mappers

import androidx.room.PrimaryKey
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.remote.dto.Result
import com.example.movieapp.domain.model.Movie


fun Result.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdropPath = backdropPath ?: "",
        genreIds = try {
            genreIds?.joinToString(",") ?: "-1,-2" // elements are strings separated by commas
        } catch (e: Exception) {
            "-1,-2"
        },
        originalLanguage = originalLanguage ?: "",
        originalTitle = originalTitle ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        posterPath = posterPath ?: "",
        releaseDate = releaseDate ?: "",
        title = title ?: "",
        video = video ?: false,
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0,
        id = id ?: -1,
        category = category
    )
}


fun MovieEntity.toMovie(
    category: String
): Movie {
    return Movie(
        adult = adult,
        backdropPath = backdropPath ,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        id = id,
        category = category,

        genreIds =  try {
            genreIds.split(",").map {
                it.toInt()
            }
        } catch (e: Exception){
            listOf(-1, -2)
        }
    )
}