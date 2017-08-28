package com.azisamirul.popularmovies2;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import com.azisamirul.popularmovies2.adapter.FavouriteMovieAdapter;
import com.azisamirul.popularmovies2.data.MovieContract;

public class FavouritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, FavouriteMovieAdapter.FavouriteMovieOnClickHandler {


    private static final int FAV_LOADER_ID = 1;
    private FavouriteMovieAdapter favouriteMovieAdapter;
    private GridLayoutManager gridLayoutManager;
    RecyclerView favRecyclerView;
    Parcelable parcelable;
    private String FAVOURITE_LISTS_KEY = "favourite_lists";
    private String FAVOURITE_LISTS_KEY_RV = "favourite_lists_rv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        favRecyclerView = (RecyclerView) findViewById(R.id.rv_favourites_movies);


        getSupportLoaderManager().initLoader(FAV_LOADER_ID, null, this);
        favRecyclerView.setHasFixedSize(true);

        favouriteMovieAdapter = new FavouriteMovieAdapter(FavouritesActivity.this, getApplicationContext());
        favRecyclerView.setAdapter(favouriteMovieAdapter);

        //References from :https://stackoverflow.com/questions/29579811/changing-number-of-columns-with-gridlayoutmanager-and-recyclerview/29582477#29582477
        if(getApplicationContext().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
         gridLayoutManager=new GridLayoutManager(FavouritesActivity.this,3);
        }else{
            gridLayoutManager=new GridLayoutManager(FavouritesActivity.this,2);
        }
        favRecyclerView.setLayoutManager(gridLayoutManager);
        if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(FAVOURITE_LISTS_KEY_RV)) {
                    parcelable = savedInstanceState.getParcelable(FAVOURITE_LISTS_KEY_RV);
                    gridLayoutManager.onRestoreInstanceState(parcelable);
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(FAV_LOADER_ID, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable parcelable1=gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(FAVOURITE_LISTS_KEY_RV,parcelable1);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor movieData = null;

            @Override
            protected void onStartLoading() {
                if (movieData != null) {
                    deliverResult(movieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver()
                            .query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry._ID);
                } catch (Exception e) {
                    Log.e("Error", "failed to asynchronously data");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                movieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle("Favourite Lists");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favouriteMovieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favouriteMovieAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int pos) {

    }
}
