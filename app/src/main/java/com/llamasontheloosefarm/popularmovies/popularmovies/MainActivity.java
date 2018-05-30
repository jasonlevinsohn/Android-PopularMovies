package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.llamasontheloosefarm.popularmovies.popularmovies.models.Movie;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.MoviesJSONUtils;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MovieGridAdapter movieAdapter;
    private GridView gridView;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = (GridView) findViewById(R.id.movies_grid_view);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData("popularity");

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

            Log.i(TAG, "**********");
            Log.i(TAG, movieUrl.toString());
            Log.i(TAG, "**********");

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

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);

            ArrayList<Movie> movieArrayList;

            if (movieData != null) {
                for (Movie movie : movieData) {

                    Log.i(TAG, "\n\n***************");
                    Log.i(TAG, movie.getTitle());
                    Log.i(TAG, "***************");
                }
                movieArrayList = new ArrayList<Movie>(Arrays.asList(movieData));
                movieAdapter = new MovieGridAdapter(MainActivity.this, movieArrayList);
                gridView.setAdapter(movieAdapter);
            }


        }


    }


    private void loadMovieData(String sortBy) {
       URL movieUrl = NetworkUtils.buildUrl(this, "popular");
       Log.i(TAG, "****************");
       Log.i(TAG, movieUrl.toString());
       Log.i(TAG, "****************");

       new FetchMovieTask().execute(sortBy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_popular) {
            Log.i(TAG, "********************");
            Log.i(TAG, "Sort Popular");
            Log.i(TAG, "********************");
            loadMovieData("popularity");
            return true;
        } else if (id == R.id.sort_toprated) {
            Log.i(TAG, "********************");
            Log.i(TAG, "Top Rated");
            Log.i(TAG, "********************");
            loadMovieData("top_rated");
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
