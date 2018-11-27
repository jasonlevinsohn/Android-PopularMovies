package com.llamasontheloosefarm.popularmovies.popularmovies.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM roommovie ORDER BY id")
    List<Movie> loadAllMovies();

    @Insert
    void insertMovie(Movie movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movieEntry);

    @Delete
    int deleteMovie(Movie movieEntry);

    @Query("SELECT * FROM roommovie WHERE movieId = :movieId")
    Movie loadTaskByMovieId(String movieId);
}
