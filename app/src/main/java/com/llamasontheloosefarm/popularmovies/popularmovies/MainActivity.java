package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.llamasontheloosefarm.popularmovies.popularmovies.data.AppDatabase;
import com.llamasontheloosefarm.popularmovies.popularmovies.data.MovieContract.*;
import com.llamasontheloosefarm.popularmovies.popularmovies.data.MovieDbHelper;
import com.llamasontheloosefarm.popularmovies.popularmovies.data.Movie;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.MoviesJSONUtils;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SAVED_SORT_BY = "sort_by";

    private MovieGridAdapter movieAdapter;
    private GridView gridView;

    private ProgressBar mLoadingIndicator;

//    private SQLiteDatabase mDb;
    private AppDatabase roomDb;
    private Cursor movieCursor;
    Toolbar toolbar;
    private static final String SORT_BY_POPULARITY = "popularity";
    private static final String SORT_BY_TOP_RATED = "top_rated";
    private static final String SORT_BY_FAVORITES = "favorites";
    private String currentSort;
    private Movie[] favoriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        MovieDbHelper dbHelper = new MovieDbHelper(this);
//        mDb = dbHelper.getReadableDatabase();
        roomDb = AppDatabase.getsInstance((getApplicationContext()));

        gridView = (GridView) findViewById(R.id.movies_grid_view);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState != null) {
            currentSort = savedInstanceState.getString(SAVED_SORT_BY);
        } else {
            currentSort = SORT_BY_POPULARITY;
        }

        // Start LiveData
        retreiveMovies();

        Stetho.initializeWithDefaults(this);

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

    }

    private void retreiveMovies() {
        Log.d(TAG, "Database: actively pulling movies from the database");
        final LiveData<List<Movie>> movies = roomDb.movieDao().loadAllMovies();
        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Database: retrieving movies from live data");
                favoriteMovies = movies.toArray(new Movie[movies.size()]);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMovieData(currentSort);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_SORT_BY, currentSort);
        super.onSaveInstanceState(outState);
    }



    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String sortBy = "";

            if (params[0] != null) {
                sortBy = params[0];
            }

            URL movieUrl = NetworkUtils.buildUrl(MainActivity.this, sortBy );

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);

                Movie[] movieModel = MoviesJSONUtils.getSimpleMovieStingsFromJSON(MainActivity.this, jsonMoviesResponse);

                return movieModel;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {

            populateMovieList(movieData);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);

        }


    }


    private void loadMovieData(String sortBy) {
        Movie[] movieData;
//       URL movieUrl = NetworkUtils.buildUrl(this, "popular");
        if (sortBy.equals("favorites")) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.INVISIBLE);
            movieData = favoriteMovies;
            if (movieData != null) {
                populateMovieList(movieData);
            }
        } else {
            new FetchMovieTask().execute(sortBy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_popular) {
            currentSort = SORT_BY_POPULARITY;
            loadMovieData("popularity");
            return true;
        } else if (id == R.id.sort_toprated) {
            currentSort = SORT_BY_TOP_RATED;
            loadMovieData("top_rated");
            return true;
        } else if (id == R.id.sort_favorites) {
            currentSort = SORT_BY_FAVORITES;
            loadMovieData("favorites");
        }

        return super.onOptionsItemSelected(item);
    }

//    private Movie[] getFavoriteMovies() {
//        final LiveData<List<Movie>> favoriteMovies;
//        Movie[] favoriteMovieArray;
//        int movieCount;
//        int counter = 0;
//        String id;
//        String title;
//        String poster;
//        String releaseDate;
//        String voteAverage;
//        String plot;


//        movieCursor = mDb.query(
//                MovieEntry.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                MovieEntry._ID
//        );
//        movieCount = movieCursor.getCount();
//        if (movieCount > 0) {
//            favoriteMovies = new Movie[movieCount];
//            while(movieCursor.moveToNext()) {
//                id = movieCursor.getString(movieCursor.getColumnIndex(MovieEntry.MOVIE_ID));
//                title = movieCursor.getString(movieCursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
//                poster = movieCursor.getString(movieCursor.getColumnIndex(MovieEntry.COLUMN_POSTER_IMAGE));
//                releaseDate = movieCursor.getString(movieCursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
//                voteAverage = movieCursor.getString(movieCursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE));
//                plot = movieCursor.getString(movieCursor.getColumnIndex(MovieEntry.COLUMN_PLOT));
//
//                Movie favMovie = new Movie(id, title, poster, releaseDate, voteAverage, plot);
//                favoriteMovies[counter] = favMovie;
//                counter++;
//
//            }
//            return favoriteMovies;
//        Log.d(TAG, "Database: Actively retrieving the movies from the database.");
//        favoriteMovies = roomDb.movieDao().loadAllMovies();
//        if (favoriteMovies.size() > 0) {
//
//            favoriteMovieArray = favoriteMovies.toArray(new Movie[favoriteMovies.size()]);
//            return favoriteMovieArray;
//
//        } else {
//            return new Movie[0];
//        }

//    }

    private void populateMovieList(Movie[] movieData) {

        final ArrayList<Movie> movieArrayList;

        if (movieData != null) {
            movieArrayList = new ArrayList<Movie>(Arrays.asList(movieData));

            // Hacks - BEGIN
//            movieAdapter = null;
//            gridView.invalidateViews();
            // Hacks - END

            movieAdapter = new MovieGridAdapter(MainActivity.this, movieArrayList);
//            movieAdapter.notifyDataSetChanged();
//            gridView.invalidateViews();
            gridView.setAdapter(movieAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Context context = MainActivity.this;
                    Class dest = ChildActivity.class;
                    Movie selectedMovie =  movieArrayList.get(i);
                    String title = selectedMovie.getTitle();
//                        String poster = selectedMovie.getPosterImage();
//                        String releaseDate = selectedMovie.getReleaseDate();
//                        String voteAverage = selectedMovie.getVoteAverage();
//                        String plot = selectedMovie.getPlot();

                    Log.d(TAG, "Movie Id Title: " + title);

                    Intent intent = new Intent(context, dest);
                    intent.putExtra("movie", selectedMovie);

                    startActivity(intent);
                }
            });
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);
//            movieAdapter.notifyDataSetChanged();
        }

    }

}
