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

import com.llamasontheloosefarm.popularmovies.popularmovies.models.Review;
import com.llamasontheloosefarm.popularmovies.popularmovies.models.Trailer;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Review[] mReviewData;
    Context context;

    public ReviewAdapter() {

    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

//        public final ImageButton mPlayButton;
        public final TextView mAuthor;
        public final TextView mReviewDescription;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthor = (TextView) view.findViewById(R.id.tv_review_author);
            mReviewDescription = (TextView) view.findViewById(R.id.tv_review_desc);
        }


    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

//        view.setBackground(context.getDrawable(R.drawable.listview_item_border));
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {

        String reviewAuthor = mReviewData[position].getAuthor();
        String reviewDesc = mReviewData[position].getContent();

        holder.mAuthor.setText(reviewAuthor);
        holder.mReviewDescription.setText(reviewDesc);

    }

    @Override
    public int getItemCount() {
        if (mReviewData == null) return 0;
        return mReviewData.length;
    }

    public void setReviewData(Review[] reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();

    }
}
