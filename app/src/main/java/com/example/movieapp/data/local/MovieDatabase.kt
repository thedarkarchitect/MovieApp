package com.example.movieapp.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.data.local.dao.MovieDao

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema =  false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}