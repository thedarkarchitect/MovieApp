package com.example.movieapp.presentation.details

import com.example.movieapp.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
