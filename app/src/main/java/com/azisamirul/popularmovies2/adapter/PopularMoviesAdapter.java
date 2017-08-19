package com.azisamirul.popularmovies2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azisamirul.popularmovies2.MovieDetails;
import com.azisamirul.popularmovies2.R;
import com.azisamirul.popularmovies2.config.Config;
import com.azisamirul.popularmovies2.data.MovieData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by azisamirul on 09/07/2017.
 */

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesViewHolder> {

    private List<MovieData> movieDataList;
    private Context context;
    public TextView original_title;
    public ImageView iv_pop_movie;
    private final PopularMoviesOnClickHandler mclickHandler;

    public interface PopularMoviesOnClickHandler {
        void onClick(int pos_movie);
    }

    public PopularMoviesAdapter(PopularMoviesOnClickHandler clickHandler, Context mContext) {
        context = mContext;
        mclickHandler = clickHandler;
    }

    public class PopularMoviesViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public PopularMoviesViewHolder(View view) {
            super(view);
            iv_pop_movie = (ImageView) view.findViewById(R.id.iv_pop_movie);
            original_title = (TextView) view.findViewById(R.id.original_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mclickHandler.onClick(adapterPosition);
            Context context1 = v.getContext();
            Intent intent = new Intent(context1, MovieDetails.class);
            intent.putExtra(Config.movieId, movieDataList.get(adapterPosition).getId());
            context1.startActivity(intent);

        }
    }

    @Override
    public PopularMoviesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new PopularMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularMoviesViewHolder popularMoviesViewHolder, int position) {
        MovieData m = movieDataList.get(position);
        String image_url = Config.IMAGE_URL + Config.IMAGE_SIZE + m.getPosterPath();
        Picasso.with(context).load(image_url).into(iv_pop_movie);
        original_title.setText(m.getOriginalTitle());
    }

    @Override
    public int getItemCount() {
        if (null == movieDataList) return 0;
        return movieDataList.size();
    }



    public void setMovieDataList(List<MovieData> movie) {
        movieDataList = movie;

    }

}
