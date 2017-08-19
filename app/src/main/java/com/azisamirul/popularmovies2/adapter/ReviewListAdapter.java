package com.azisamirul.popularmovies2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.azisamirul.popularmovies2.R;
import com.azisamirul.popularmovies2.ReviewDetails;
import com.azisamirul.popularmovies2.config.Config;
import com.azisamirul.popularmovies2.data.ReviewList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azisamirul on 11/08/2017.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewListViewHolder> {
    private List<ReviewList> reviewDataList;
    public TextView txtReviewAuthor;
    private final ReviewListOnClickHandler mClickHandler;

    public interface ReviewListOnClickHandler {
        void onClick(int position);
    }

    public ReviewListAdapter(ReviewListOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class ReviewListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ReviewListViewHolder(View view) {
            super(view);
            txtReviewAuthor = (TextView) view.findViewById(R.id.tv_review_author);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
            Context ct = v.getContext();
            Intent i = new Intent(ct, ReviewDetails.class);
            //List ls=new ArrayList<ReviewList>(reviewDataList.get(adapterPosition));
            i.putExtra(Config.REVIEW_LIST_POSITION, adapterPosition);
            i.putParcelableArrayListExtra(Config.REVIEW_CONTENT, new ArrayList<ReviewList>(reviewDataList));

            ct.startActivity(i);
        }
    }

    @Override
    public ReviewListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_lists;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewListViewHolder holder, int position) {
        ReviewList reviewList = reviewDataList.get(position);
        txtReviewAuthor.setText(reviewList.getAuthor());
    }

    @Override
    public int getItemCount() {
        if (reviewDataList == null) return 0;
        return reviewDataList.size();
    }

    public void setReviewDataList(List<ReviewList> review) {
        reviewDataList = review;
        notifyDataSetChanged();
    }
}
