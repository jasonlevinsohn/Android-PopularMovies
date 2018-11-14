package com.llamasontheloosefarm.popularmovies.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import com.llamasontheloosefarm.popularmovies.popularmovies.models.Movie;
import com.llamasontheloosefarm.popularmovies.popularmovies.models.Review;
import com.llamasontheloosefarm.popularmovies.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public final class MoviesJSONUtils {

    public static Review[] getSimpleReviewsStringsFromJSON(Context context, String reviewJSON) throws JSONException {

        final String TAG = "Review JSON Extraction";

        final String REVIEW_LIST = "results";

        // JSON Fields
        final String REVIEW_ID = "id";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String REVIEW_URL = "url";

        // Status Code
        final String REVIEW_STATUS_CODE = "cod";

        Review[] parsedReviewData = null;

        JSONObject reviewJson = new JSONObject(reviewJSON);

        if (reviewJson.has(REVIEW_STATUS_CODE)) {
            int errorCode = reviewJson.getInt(REVIEW_STATUS_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray reviewArray = reviewJson.getJSONArray(REVIEW_LIST);
        parsedReviewData = new Review[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++) {

            JSONObject trailerObj = reviewArray.getJSONObject(i);


            String reviewId = trailerObj.getString(REVIEW_ID);
            String reviewAuthor = trailerObj.getString(REVIEW_AUTHOR);
            String reviewContent = trailerObj.getString(REVIEW_CONTENT);
            String reviewUrl = trailerObj.getString(REVIEW_URL);

            Log.d(TAG, "Review Author: " + reviewAuthor);
            Log.d(TAG, "Review Desc: " + reviewContent);

            Review review = new Review(reviewId, reviewAuthor, reviewContent, reviewUrl);
            parsedReviewData[i] = review;
        }

        return parsedReviewData;
    }

    public static Trailer[] getSimpleTrailerStringsFromJSON(Context context, String trailerJSON) throws JSONException {

        final String TAG = "Trailer JSON Extraction";

        final String TRAILER_LIST = "results";

        // JSON Fields
        final String TRAILER_ID = "id";
        final String TRAILER_KEY = "key";
        final String TRAILER_SITE = "site";
        final String TRAILER_TYPE = "type";
        final String TRAILER_NAME = "name";

        // Status Code
        final String TRAILER_STATUS_CODE = "cod";

        Trailer[] parsedTrailerData = null;

        JSONObject trailerJson = new JSONObject(trailerJSON);

        if (trailerJson.has(TRAILER_STATUS_CODE)) {
            int errorCode = trailerJson.getInt(TRAILER_STATUS_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray trailerArray = trailerJson.getJSONArray(TRAILER_LIST);
        parsedTrailerData = new Trailer[trailerArray.length()];

        for (int i = 0; i < trailerArray.length(); i++) {

            JSONObject trailerObj = trailerArray.getJSONObject(i);


//             trailerIdInt = trailerObj.getString(TRAILER_ID);
            String trailerId = trailerObj.getString(TRAILER_ID);
            String trailerKey = trailerObj.getString(TRAILER_KEY);
            String trailerSite = trailerObj.getString(TRAILER_SITE);
            String trailerType = trailerObj.getString(TRAILER_TYPE);
            String trailerName = trailerObj.getString(TRAILER_NAME);

            Log.d(TAG, "Trailer Type: " + trailerType);

            Trailer trailer = new Trailer(trailerId, trailerKey, trailerSite, trailerType, trailerName);
            parsedTrailerData[i] = trailer;
        }

        return parsedTrailerData;
    }


    public static Movie[] getSimpleMovieStingsFromJSON(Context context, String movieJSON) throws JSONException {

        final String TAG = "Movie JSON Extraction";

        final String MOVIE_LIST = "results";


        final String MOVIE_ID = "id";
        final String MOVIE_MESSAGE_CODE = "cod";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";
        final String OVERVIEW = "overview";

        Movie[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJSON);

        if (movieJson.has(MOVIE_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(MOVIE_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);
        parsedMovieData = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieObj = movieArray.getJSONObject(i);
            int movieIdInt = movieObj.getInt(MOVIE_ID);
            String movieId = Integer.toString(movieIdInt);
            String title = movieObj.getString(MOVIE_TITLE);
            String posterImage = movieObj.getString(MOVIE_POSTER_PATH);
            String releaseDate = movieObj.getString(RELEASE_DATE);
            Double voteAverageLong = movieObj.getDouble(VOTE_AVERAGE);
            String voteAverageString = voteAverageLong.toString();
            String plot = movieObj.getString(OVERVIEW);

            Movie movie = new Movie(movieId, title, posterImage, releaseDate, voteAverageString, plot);
            parsedMovieData[i] = movie;

        }

        return parsedMovieData;
    }


}
