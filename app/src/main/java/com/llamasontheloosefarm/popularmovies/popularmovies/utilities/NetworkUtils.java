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

    private static final String MOVIES_DB_BASE_URL =
            "https://api.themoviedb.org";
    private static final String MOVIEDB_POSTER_BASE_URL = "https://image.tmdb.org";

    private static final String API_KEY = "api_key";
    private static final String LANGUAGE_KEY = "language";
    private static final String LANGUAGE = "en-US";
    private static String SORT_BY = "popular";

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
            SORT_BY = "top_rated";
        } else if (sortBy.length() == 0 || sortBy == "popularity") {
            SORT_BY = "popular";
        }

        Uri builtUri = Uri.parse(MOVIES_DB_BASE_URL).buildUpon()
                .encodedPath("3/movie/" + SORT_BY)
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(LANGUAGE_KEY, LANGUAGE)
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
