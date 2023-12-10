package com.example.movieapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.MovieListRepository
import com.example.movieapp.util.Category
import com.example.movieapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieListRepository
): ViewModel() {

    private var _state = MutableStateFlow(MovieListState())
    val state = _state

    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }

    fun onEvent(event: MovieListUiEvent) {
        when(event) {
            MovieListUiEvent.Navigate -> {
                _state.update {
                    it.copy(
                        isCurrentPopularScreen = !state.value.isCurrentPopularScreen
                    )
                }
            }
            is MovieListUiEvent.Pagination -> {
                if (event.category == Category.POPULAR) {
                    getPopularMovieList(true)//this force fetch is true because the 1st page is exceeded that means an api call needs to be made just incase the
                } else if (event.category == Category.UPCOMING) {
                    getUpcomingMovieList(true)
                }
            }
        }
    }

    private fun getPopularMovieList(forceFetchFromRemote: Boolean){
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            repository.getMovieList(
                forceFetchFromRemote,
                category = Category.POPULAR,
                page = _state.value.upcomingMovieListPage
            ).collectLatest {  result ->//Resource<List<Movie>>
                when(result) {
                    is Resource.Error -> {
                        _state.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let {  popularList ->//List<Movie>
                            _state.update {
                                it.copy(
                                    popularMovieList = state.value.popularMovieList + popularList.shuffled()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean){
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            repository.getMovieList(
                forceFetchFromRemote = true,
                category = Category.UPCOMING,
                page = _state.value.upcomingMovieListPage
            ).collectLatest { result ->//Resource<List<Movie>>
                when(result) {
                    is Resource.Error -> {
                        _state.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let {  upcomingList ->//List<Movie>
                            _state.update {
                                it.copy(
                                    upcomingMovieList = state.value.upcomingMovieList + upcomingList.shuffled()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}