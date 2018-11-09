package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.llamasontheloosefarm.popularmovies.popularmovies.models.Movie;
import com.llamasontheloosefarm.popularmovies.popularmovies.models.Trailer;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.MoviesJSONUtils;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class ChildActivity extends AppCompatActivity {

    private String TAG = ChildActivity.class.getSimpleName();
    private TextView mTitle;
    private ImageView mMoviePoster;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        String movieTitle;
        String moviePosterString;
        Uri moviePosterUri;
        String movieReleaseDate;
        String movieVoteAverage;
        String moviePlot;
        Movie selectedMovie;

        Intent fromIntent = getIntent();

        if (fromIntent.hasExtra("movie")) {
            selectedMovie = fromIntent.getParcelableExtra("movie");
        } else {
            selectedMovie = new Movie("","", "", "", "", "");
        }

        Log.d(TAG, "Movie Id: " + selectedMovie.getMovieId());
        Log.d(TAG, "Movie Id Title: " + selectedMovie.getTitle());

        moviePosterString = selectedMovie.getPosterImage();
        moviePosterUri = NetworkUtils.buildPosterUrl(moviePosterString);

        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster);
        Picasso.get().load(moviePosterUri).into(mMoviePoster);


        movieTitle = selectedMovie.getTitle();

        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mTitle.setText(movieTitle);

        movieReleaseDate = selectedMovie.getReleaseDate();

        mReleaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        mReleaseDate.setText(movieReleaseDate);

        movieVoteAverage = selectedMovie.getVoteAverage();

        mVoteAverage = (TextView) findViewById(R.id.tv_movie_vote_average);
        mVoteAverage.setText(movieVoteAverage);

       moviePlot = selectedMovie.getPlot();

       mPlot = (TextView) findViewById(R.id.tv_movie_plot);
       mPlot.setText(moviePlot);

       loadTrailerData(selectedMovie.getMovieId());

       // Setup Recycler View for displaying Trailers.
        RecyclerView trailerRv = (RecyclerView)


    }

    public class FetchMovieTrailers extends AsyncTask<String, Void, Trailer[]> {
        @Override
        protected Trailer[] doInBackground(String... params) {
//            return new Trailer[0];
            if (params.length == 0) {
                return null;
            }

            String movieId = "";

            if (params[0] != null) {
                movieId = params[0];
            }

            URL trailerUrl = NetworkUtils.buildTrailerUrl(ChildActivity.this, movieId);

            try {
                String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(trailerUrl);

                Trailer[] trailerModel = MoviesJSONUtils.getSimpleTrailerStringsFromJSON(ChildActivity.this, jsonTrailersResponse);

                return trailerModel;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            super.onPostExecute(trailers);

            final ArrayList<Trailer> trailerArrayList;

            if (trailers != null) {
                trailerArrayList = new ArrayList<>(Arrays.asList(trailers));
                for (Trailer trailer : trailerArrayList) {
                    Log.d(TAG, "Trailer Name: " + trailer.getName());
                }
            }
        }

    }

    private void loadTrailerData(String movieId) {
        new FetchMovieTrailers().execute(movieId);
    }
}
