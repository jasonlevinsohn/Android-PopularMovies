package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.llamasontheloosefarm.popularmovies.popularmovies.data.Movie;
import com.llamasontheloosefarm.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieGridAdapter extends ArrayAdapter<Movie> {
    private static final String TAG = MovieGridAdapter.class.getSimpleName();


    public MovieGridAdapter(Activity context, ArrayList<Movie> popularMovies) {
        super(context, 0, popularMovies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        Uri posterImageUri = NetworkUtils.buildPosterUrl(movie.getPosterImage());

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        ImageView posterImageView = (ImageView) convertView.findViewById(R.id.movie_poster_image);

        // Scales the image to fit the entire Image View
        posterImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Picasso.get().load(posterImageUri).into(posterImageView);

        return convertView;
    }
}



