package com.example.movieapp.presentation.home

sealed class MovieListUiEvent {
    data class Pagination(val category: String) : MovieListUiEvent()
    data object Navigate : MovieListUiEvent()

}