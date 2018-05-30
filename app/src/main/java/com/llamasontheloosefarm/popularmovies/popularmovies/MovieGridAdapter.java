package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.llamasontheloosefarm.popularmovies.popularmovies.models.Movie;

import java.util.ArrayList;

public class MovieGridAdapter extends ArrayAdapter<Movie> {
    private static final String TAG = MovieGridAdapter.class.getSimpleName();
    private ArrayList<Movie> mMovieGridData;

    public MovieGridAdapter(Activity context, ArrayList<Movie> popularMovies) {
        super(context, 0, popularMovies);
        this.mMovieGridData = popularMovies;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.movie_title);
        titleView.setText(movie.getTitle());

        return convertView;
    }
}



