package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
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

            final ArrayList<Movie> movieArrayList;

            if (movieData != null) {
                movieArrayList = new ArrayList<Movie>(Arrays.asList(movieData));
                movieAdapter = new MovieGridAdapter(MainActivity.this, movieArrayList);
                gridView.setAdapter(movieAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Context context = MainActivity.this;
                        Class dest = ChildActivity.class;
                        Movie selectedMovie =  movieArrayList.get(i);
                        String title = selectedMovie.getTitle();
                        String poster = selectedMovie.getPosterImage();
                        String releaseDate = selectedMovie.getReleaseDate();
                        String voteAverage = selectedMovie.getVoteAverage();
                        String plot = selectedMovie.getPlot();

                        Intent intent = new Intent(context, dest);
                        intent.putExtra("movie", selectedMovie);

                        startActivity(intent);
                    }
                });
            }

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);



        }


    }


    private void loadMovieData(String sortBy) {
       URL movieUrl = NetworkUtils.buildUrl(this, "popular");
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
            loadMovieData("popularity");
            return true;
        } else if (id == R.id.sort_toprated) {
            loadMovieData("top_rated");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
