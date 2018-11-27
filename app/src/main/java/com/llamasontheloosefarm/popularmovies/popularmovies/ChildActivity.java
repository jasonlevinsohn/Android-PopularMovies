package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.llamasontheloosefarm.popularmovies.popularmovies.data.AppDatabase;
import com.llamasontheloosefarm.popularmovies.popularmovies.data.MovieDbHelper;
import com.llamasontheloosefarm.popularmovies.popularmovies.data.Movie;
import com.llamasontheloosefarm.popularmovies.popularmovies.models.Review;
import com.llamasontheloosefarm.popularmovies.popularmovies.models.Trailer;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.MoviesJSONUtils;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import com.llamasontheloosefarm.popularmovies.popularmovies.data.MovieContract.*;

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
    private TextView mAddToFavorites;
    private TextView mRemoveFromFavorites;

    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

//    private SQLiteDatabase mDb;
    private AppDatabase roomDb;
    private Cursor movieCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);



        String movieId;
        String movieTitle;
        String moviePosterString;
        Uri moviePosterUri;
        String movieReleaseDate;
        String movieVoteAverage;
        final String moviePlot;
        final Movie selectedMovie;

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


       // Favorites Section - BEGIN
       mAddToFavorites = (TextView) findViewById(R.id.tv_add_to_favorites);
       mRemoveFromFavorites = (TextView) findViewById(R.id.tv_remove_from_favorites);

       mAddToFavorites.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               addMovieToFavorites(selectedMovie);
           }
       });

       mRemoveFromFavorites.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               boolean movieRemoved;
               movieRemoved = removeMovieFromFavorites(selectedMovie);

               if (movieRemoved) {
                   mAddToFavorites.setVisibility(View.VISIBLE);
                   mRemoveFromFavorites.setVisibility(View.INVISIBLE);
               }
           }
       });

       mAddToFavorites.setVisibility(View.VISIBLE);
       mRemoveFromFavorites.setVisibility(View.INVISIBLE);
       // Favorites Section - END

       loadTrailerReviewData(selectedMovie.getMovieId());


       // Setup Recycler View for displaying Trailers.
       mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recycler_view);
       mReviewRecyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
        LinearLayoutManager trailerLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager reviewLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);

        mTrailerRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter();
        mReviewAdapter = new ReviewAdapter();

        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        // Initialize Database
//        MovieDbHelper dbHelper = new MovieDbHelper(this);
//        mDb = dbHelper.getWritableDatabase();

        // Using Room Database
        roomDb = AppDatabase.getsInstance(getApplicationContext());

        // Check if this movie is in our Favorites Database.
//        String selection = MovieEntry.MOVIE_ID + " = ?";
//        String[] selectionArgs = { selectedMovie.getMovieId() };

//        movieCursor = mDb.query(
//                MovieEntry.TABLE_NAME,
//                null,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
//        int movieCount = movieCursor.getCount();
//
//        if (movieCount > 0) {
//            mAddToFavorites.setVisibility(View.INVISIBLE);
//            mRemoveFromFavorites.setVisibility(View.VISIBLE);
//        }

//        while(movieCursor.moveToNext()) {
//            String dbTitle = movieCursor.getString(movieCursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
//            Log.d(TAG, "Movie Cursor Title: " + dbTitle);
//        }

        // Check to see if the movie is already in our room database.
        Movie favoriteMovie = roomDb.movieDao().loadTaskByMovieId(selectedMovie.getMovieId());
        if (favoriteMovie != null) {
            mAddToFavorites.setVisibility(View.INVISIBLE);
            mRemoveFromFavorites.setVisibility(View.VISIBLE);
        }


        Stetho.initializeWithDefaults(this);
    }

    public class FetchMovieReviews extends AsyncTask<String, Void, Review[]> {
        @Override
        protected Review[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String movieId = "";

            if (params[0] != null) {
                movieId = params[0];
            }

            URL reviewUrl = NetworkUtils.buildTrailerReviewUrl(ChildActivity.this, movieId, true);

            try {
                String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);

                Review[] reviewModel = MoviesJSONUtils.getSimpleReviewsStringsFromJSON(ChildActivity.this, jsonReviewsResponse);

                return reviewModel;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            super.onPostExecute(reviews);

//            final ArrayList<Review> reviewArrayList;

            mReviewAdapter.setReviewData(reviews);


        }
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

            URL trailerUrl = NetworkUtils.buildTrailerReviewUrl(ChildActivity.this, movieId, false);

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

            final ArrayList<Trailer> videoArrayList;
            final ArrayList<Trailer> trailerArrayList = new ArrayList<>();

            if (trailers != null) {
                videoArrayList = new ArrayList<>(Arrays.asList(trailers));
                for (Trailer video : videoArrayList) {
                    Log.d(TAG, "Trailer Name: " + video.getName());
                    String type = video.getType();
                    if (type.equals("Trailer")) {
                        trailerArrayList.add(video);
                    } else if (type.equals("Teaser")) {
                        trailerArrayList.add(video);
                    }
                }

                Trailer[] filteredTrailers = trailerArrayList.toArray(new Trailer[0]);

                mTrailerAdapter.setTrailerData(filteredTrailers);


            } else {
                Log.d(TAG, "Trailer Name: Error fetching Trailers");
            }
        }

    }

    private void loadTrailerReviewData(String movieId) {
        new FetchMovieTrailers().execute(movieId);
        new FetchMovieReviews().execute(movieId);
    }

    private void addMovieToFavorites(Movie selectedMovie) {
//        String id = selectedMovie.getMovieId();
        String title = selectedMovie.getTitle();
//        String poster = selectedMovie.getPosterImage();
//        String releaseDate = selectedMovie.getReleaseDate();
//        String voteAverage = selectedMovie.getVoteAverage();
//        String plot = selectedMovie.getPlot();
//
//        ContentValues cv = new ContentValues();

//        cv.put(MovieEntry.MOVIE_ID, id);
//        cv.put(MovieEntry.COLUMN_TITLE, title);
//        cv.put(MovieEntry.COLUMN_POSTER_IMAGE, poster);
//        cv.put(MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
//        cv.put(MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
//        cv.put(MovieEntry.COLUMN_PLOT, plot);

        roomDb.movieDao().insertMovie(selectedMovie);

        Toast.makeText(ChildActivity.this,
                title + " has been added to the favorites list.", Toast.LENGTH_SHORT).show();

        mAddToFavorites.setVisibility(View.INVISIBLE);
        mRemoveFromFavorites.setVisibility(View.VISIBLE);

//        return mDb.insert(MovieEntry.TABLE_NAME, null, cv);

    }

    private boolean removeMovieFromFavorites(Movie selected) {

        Movie movieToRemove = roomDb.movieDao().loadTaskByMovieId(selected.getMovieId());

        int movieRemoved = roomDb.movieDao().deleteMovie(movieToRemove);

//        return mDb.delete(
//                MovieEntry.TABLE_NAME,
//                MovieEntry.MOVIE_ID + "=" + id,
//                null
//        ) > 0;
        return (movieRemoved > 0);

    }
}
