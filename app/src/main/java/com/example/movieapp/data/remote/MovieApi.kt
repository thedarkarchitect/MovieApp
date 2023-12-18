package com.example.movieapp.data.remote

import com.example.movieapp.data.remote.dto.MovieListDto
import com.example.movieapp.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apikey: String = API_KEY
    ): MovieListDto
}