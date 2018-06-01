package com.llamasontheloosefarm.popularmovies.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;

import com.llamasontheloosefarm.popularmovies.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIESDB_BASE_URL =
            "https://api.themoviedb.org/3/discover/movie";
    private static final String MOVIEDB_POSTER_BASE_URL = "https://image.tmdb.org";

    private static final String API_KEY = "api_key";
    private static final String LANGUAGE_KEY = "language";
    private static final String LANGUAGE = "en-US";
    private static final String SORT_BY_KEY = "sort_by";
    private static String SORT_BY = "popularity.desc";
    private static final String INCLUDE_ADULT_KEY = "include_adult";
    private static final String INCLUDE_ADULT = "false";
    private static final String INCLUDE_VIDEO_KEY = "include_video";
    private static final String INCLUDE_VIDEO = "false";

//    https://api.themoviedb.org/3/discover/movie?api_key=60007b942d38fc29b45b19302efb7969&language=en-US
// &sort_by=popularity.desc&include_adult=false&include_video=false
//    https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg

    /** Builds the URL for the movie poster image
     * @param posterImageName accepts the poster file name
     * @return The url used to get the poster image
     */
    public static Uri buildPosterUrl(String posterImageName) {
        if (posterImageName != null) {
            Uri buildUri = Uri.parse(MOVIEDB_POSTER_BASE_URL).buildUpon()
                    .encodedPath("t/p/w185" + posterImageName)
                    .build();

            return buildUri;
        } else {
            return null;
        }

    }

    /**
     * @param sortBy accepts either `popular` or `recent`
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(Context context, String sortBy) {
        String apiKey = context.getString(R.string.THE_MOVIE_DB_API_KEY);

        if (sortBy == "top_rated") {
            SORT_BY = "vote_average.desc";
        } else if (sortBy.length() == 0 || sortBy == "popularity") {
            SORT_BY = "popularity.desc";
        }

        Uri builtUri = Uri.parse(MOVIESDB_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(LANGUAGE_KEY, LANGUAGE)
                .appendQueryParameter(SORT_BY_KEY, SORT_BY)
                .appendQueryParameter(INCLUDE_ADULT_KEY, INCLUDE_ADULT)
                .appendQueryParameter(INCLUDE_VIDEO_KEY, INCLUDE_VIDEO)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
