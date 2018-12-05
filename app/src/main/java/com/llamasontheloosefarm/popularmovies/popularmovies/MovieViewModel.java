package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.llamasontheloosefarm.popularmovies.popularmovies.data.AppDatabase;
import com.llamasontheloosefarm.popularmovies.popularmovies.data.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        AppDatabase roomDb = AppDatabase.getsInstance(this.getApplication());
        Log.d(TAG, "Database: Actively retrieving the tasks from the database");
        movies = roomDb.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
