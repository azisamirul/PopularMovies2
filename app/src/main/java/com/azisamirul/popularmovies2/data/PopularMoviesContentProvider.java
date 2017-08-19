package com.azisamirul.popularmovies2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.azisamirul.popularmovies2.data.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by azisamirul on 06/08/2017.
 */

public class PopularMoviesContentProvider extends ContentProvider {
    public static final int FAVOURITE_MOVIE=100;
    public static final int FAVOURITE_MOVIE_WITH_ID = 101;

    private static final UriMatcher matcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_POPULAR_MOVIES,FAVOURITE_MOVIE);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_POPULAR_MOVIES + "/*", FAVOURITE_MOVIE_WITH_ID);
        return uriMatcher;
    }

    private PopularMoviesDbHelper moviesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        moviesDbHelper = new PopularMoviesDbHelper(context);
        return true;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        int match = matcher.match(uri);
        Uri uri1;

        switch (match) {
            case FAVOURITE_MOVIE:
                long id = db.insert(TABLE_NAME, null, values);

                if (id > 0) {
                    uri1 = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                   throw new SQLiteException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri1;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = moviesDbHelper.getReadableDatabase();
       // int match = matcher.match(uri);
        Cursor cursor;

            cursor = db.query(TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);

//        switch (match) {
//            case FAVOURITE_MOVIE:
//                cursor = db.query(TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder);
//                Log.d("Fav :","table");
//                break;
//            case FAVOURITE_MOVIE_WITH_ID:
//                cursor = db.query(TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder);
//                Log.d("Fav :","with id");
//break;
//            default:
//                throw new UnsupportedOperationException("Unknown uri " + uri);
//        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        int match = matcher.match(uri);
        int favDeleted;

        switch (match) {
            case FAVOURITE_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                Log.d("Delete id",id);
                favDeleted = db.delete(TABLE_NAME, MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?", new String[]{id});
                Log.d("Fav Deleted ",String.valueOf(favDeleted));
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        if (favDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
