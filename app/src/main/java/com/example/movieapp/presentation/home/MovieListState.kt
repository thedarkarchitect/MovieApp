package com.example.movieapp.presentation.home

import com.example.movieapp.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,
    val popularMovieListPage: Int = 1,//tracks the pages of the api reponse
    val upcomingMovieListPage: Int = 1,
    val isCurrentPopularScreen: Boolean = true,//this tracks if we on the popular screen or upcoming screen
    val popularMovieList: List<Movie> = emptyList(),//tracks the list is available or not
    val upcomingMovieList: List<Movie> = emptyList()
)
