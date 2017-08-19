package com.azisamirul.popularmovies2.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azisamirul.popularmovies2.MovieDetails;
import com.azisamirul.popularmovies2.R;
import com.azisamirul.popularmovies2.config.Config;
import com.azisamirul.popularmovies2.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by azisamirul on 06/08/2017.
 */
public class FavouriteMovieAdapter extends RecyclerView.Adapter<FavouriteMovieAdapter.FavViewHolder> {
    private Cursor cursor;
    private Context context;
    private final FavouriteMovieOnClickHandler favouriteMovieOnClickHandler;
    int idIndex, movieIdIndex, originalTitleIndex, imagePathIndex;


    public interface FavouriteMovieOnClickHandler {
        void onClick(int pos);
    }

    public FavouriteMovieAdapter(FavouriteMovieOnClickHandler clickHandler, Context context) {
        this.context = context;
        favouriteMovieOnClickHandler = clickHandler;
    }

    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_list_item, parent, false);
        return new FavViewHolder(view);
    }


    @Override
    public void onBindViewHolder(FavViewHolder holder, int position) {
        idIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
        movieIdIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        originalTitleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
        imagePathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_PATH);

        cursor.moveToPosition(position);

        int id = cursor.getInt(idIndex);
        String movieId = cursor.getString(movieIdIndex);
        String originalTitle = cursor.getString(originalTitleIndex);
        String imagePath = cursor.getString(imagePathIndex);

        holder.itemView.setTag(movieId);


        Log.d("Bind ", imagePath);
        holder.fav_original_title.setText(originalTitle);
        holder.fav_original_title.setText(originalTitle);

        String image_url = Config.IMAGE_URL + Config.IMAGE_SIZE + imagePath;
        Picasso.with(context).load(image_url).into(holder.iv_fav_movie);


    }

    @Override
    public int getItemCount() {

        if (cursor == null) return 0;
        return cursor.getCount();
    }

    class FavViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public TextView fav_original_title;
        public ImageView iv_fav_movie;

        public FavViewHolder(View itemView) {
            super(itemView);
            fav_original_title = (TextView) itemView.findViewById(R.id.fav_original_title);
            iv_fav_movie = (ImageView) itemView.findViewById(R.id.iv_fav_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

//            int adapterPosition = getAdapterPosition();
//
//            mclickHandler.onClick(adapterPosition);
//            Context context1 = v.getContext();
//            Intent intent = new Intent(context1, MovieDetails.class);
//            intent.putExtra(Config.movieId, movieDataList.get(adapterPosition).getId());
//            context1.startActivity(intent);
//

            int adapterPosition = getAdapterPosition();
            favouriteMovieOnClickHandler.onClick(adapterPosition);
            Context ct = v.getContext();
            cursor.moveToPosition(adapterPosition);
            String movId = cursor.getString(movieIdIndex);
            String movTitle = cursor.getString(originalTitleIndex);
            Intent i = new Intent(ct, MovieDetails.class);

            i.putExtra(Config.movieId, movId);
            i.putExtra(Config.isFavourite,true);
//            i.putExtra(Config.);
            i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
            ct.startActivity(i);

            // Toast.makeText(ct,"Clicked "+movId+movTitle,Toast.LENGTH_LONG).show();
        }
    }

    public Cursor swapCursor(Cursor c) {
        if (cursor == c) {
            return null;
        }
        Cursor temp = cursor;
        this.cursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
