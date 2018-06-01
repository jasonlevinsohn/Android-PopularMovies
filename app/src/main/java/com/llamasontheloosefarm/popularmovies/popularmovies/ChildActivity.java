package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

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

        Intent fromIntent = getIntent();

        if (fromIntent.hasExtra("moviePoster")) {
            moviePosterString = fromIntent.getStringExtra(("moviePoster"));
            moviePosterUri = NetworkUtils.buildPosterUrl(moviePosterString);

            mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster);
            Picasso.get().load(moviePosterUri).into(mMoviePoster);
        }

        if (fromIntent.hasExtra("movieTitle")) {
            movieTitle = fromIntent.getStringExtra("movieTitle");

            mTitle = (TextView) findViewById(R.id.tv_movie_title);
            mTitle.setText(movieTitle);
        }

        if (fromIntent.hasExtra("movieReleaseDate")) {
            movieReleaseDate = fromIntent.getStringExtra("movieReleaseDate");

            mReleaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
            mReleaseDate.setText(movieReleaseDate);
        }

        if (fromIntent.hasExtra("movieVoteAverage")) {
            movieVoteAverage = fromIntent.getStringExtra("movieVoteAverage");

            mVoteAverage = (TextView) findViewById(R.id.tv_movie_vote_average);
            mVoteAverage.setText(movieVoteAverage);
        }

        if (fromIntent.hasExtra("moviePlot")) {
           moviePlot = fromIntent.getStringExtra("moviePlot");

           mPlot = (TextView) findViewById(R.id.tv_movie_plot);
           mPlot.setText(moviePlot);
        }


    }
}
