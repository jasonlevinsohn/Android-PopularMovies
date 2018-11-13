package com.llamasontheloosefarm.popularmovies.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.llamasontheloosefarm.popularmovies.popularmovies.models.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private Trailer[] mTrailerData;
    Context context;

    public TrailerAdapter() {

    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageButton mPlayButton;
        public final TextView mTrailerDescription;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            mPlayButton = (ImageButton) view.findViewById(R.id.ib_trailer_play_button);
            mTrailerDescription = (TextView) view.findViewById(R.id.tv_trailer_description);
        }


    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

//        view.setBackground(context.getDrawable(R.drawable.listview_item_border));
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder holder, int position) {
        String trailerDesc = mTrailerData[position].getName();
        final String trailerKey = mTrailerData[position].getKey();
        holder.mTrailerDescription.setText(trailerDesc);



        holder.mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,
//                        "Trailer with key " + trailerKey + " has been clicked.", Toast.LENGTH_SHORT).show();
                playYouTubeVideo(trailerKey);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mTrailerData == null) return 0;
        return mTrailerData.length;
    }

    public void setTrailerData(Trailer[] trailerData) {
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }

    private void playYouTubeVideo (String videoId) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));

        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
