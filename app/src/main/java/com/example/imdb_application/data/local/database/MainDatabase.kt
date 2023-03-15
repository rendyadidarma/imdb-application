package com.example.imdb_application.data.local.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("select * from movie_table")
    fun getMovies(): Flow<List<MovieEntity>>

    @Query("select * from movie_detail where id=:id")
    fun getDetail(id: String): Flow<DetailEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = MovieEntity::class)
    fun insertAll(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = DetailEntity::class)
    fun insertDetail(detail: DetailEntity)

    @Query("select (select count(*) from movie_table) == 0")
    fun isMovieEmpty() : Flow<Boolean>

    @Query("select (select count(*) from movie_detail where id=:id) == 0")
    fun isDetailEmpty(id : String) : Flow<Boolean>
}

@Database(entities = [MovieEntity::class, DetailEntity::class], version = 3)
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


