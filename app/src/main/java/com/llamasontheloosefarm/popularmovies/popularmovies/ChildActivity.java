package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.llamasontheloosefarm.popularmovies.popularmovies.models.Movie;
import com.llamasontheloosefarm.popularmovies.popularmovies.models.Trailer;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

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

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }
}
