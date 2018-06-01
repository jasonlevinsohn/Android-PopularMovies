package com.llamasontheloosefarm.popularmovies.popularmovies.utilities;

import android.content.Context;

import com.llamasontheloosefarm.popularmovies.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public final class MoviesJSONUtils {

    public static Movie[] getSimpleMovieStingsFromJSON(Context context, String movieJSON) throws JSONException {

        final String TAG = "Movie JSON Extraction";

        final String MOVIE_LIST = "results";


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
            String title = movieObj.getString(MOVIE_TITLE);
            String posterImage = movieObj.getString(MOVIE_POSTER_PATH);
            String releaseDate = movieObj.getString(RELEASE_DATE);
            Double voteAverageLong = movieObj.getDouble(VOTE_AVERAGE);
            String voteAverageString = voteAverageLong.toString();
            String plot = movieObj.getString(OVERVIEW);

            Movie movie = new Movie(title, posterImage, releaseDate, voteAverageString, plot);
            parsedMovieData[i] = movie;

        }

        return parsedMovieData;
    }


}