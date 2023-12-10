package com.example.movieapp.data.repository

import com.example.movieapp.data.local.MovieDatabase
import com.example.movieapp.data.remote.MovieApi
import com.example.movieapp.domain.mappers.toMovie
import com.example.movieapp.domain.mappers.toMovieEntity
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieListRepository
import com.example.movieapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val movieDb: MovieDatabase
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> =
        flow {
            //first check the db and then make an api call if nothing is cached
            emit(Resource.Loading(isLoading = true))

            val localMovieList = movieDb.movieDao.getMovieListByCategory(category) // get movies from the db of a particular category

            val shouldLoadDbMovies = localMovieList.isNotEmpty() && !forceFetchFromRemote //condition to check the if movies are in the db

            if(shouldLoadDbMovies) {
                emit(Resource.Success(
                        data = localMovieList.map{ moveEntity ->
                            moveEntity.toMovie(category)//map what in the db toMovie model used in the ui
                        }
                    )
                )
                return@flow
            }

            val movieListFromApi  = try { // api call if the db is empty
                api.getMovieList(category, page)
            } catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies", data = null))
                return@flow
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies", data = null))
                return@flow
            } catch (e: Exception){
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies", data = null))
                return@flow
            }

            val movieEntities = movieListFromApi.data?.results.let { resultList -> //data from the api mapped from api to entity
                resultList?.map {
                    it.toMovieEntity(category)
                }
            }

            movieDb.movieDao.upsertMovieList(movieEntities ?: emptyList())

            emit(Resource.Success(
                    data = movieEntities?.map {
                        it.toMovie(category) //new data mapped to movie model used in ui
                    }
                )
            )

            emit(Resource.Loading(isLoading = false))
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>>  =
        flow {
            emit(Resource.Loading(true))

            val movieEntity = movieDb.movieDao.getMovieById(id)
            try {
                emit(
                    Resource.Success(
                        movieEntity.toMovie(movieEntity.category)
                    )
                )
                emit(
                    Resource.Loading(false)
                )
            } catch (e: Exception) {
                emit(Resource.Loading(false))
            }
        }
}