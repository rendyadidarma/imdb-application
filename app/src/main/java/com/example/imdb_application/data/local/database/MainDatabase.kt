package com.example.imdb_application.data.local.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("select * from movie_table")
    fun getMovies(): Flow<List<MovieEntity>>

    @Query("select * from movie_table where id=:id")
    fun getMovie(id: String): Flow<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(movies: List<MovieEntity>)
}

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, "items")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}


