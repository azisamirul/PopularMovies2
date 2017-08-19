package com.azisamirul.popularmovies2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by azisamirul on 06/08/2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.azisamirul.popularmovies2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_POPULAR_MOVIES = "popularmovies";


    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI
                        .buildUpon()
                        .appendEncodedPath(PATH_POPULAR_MOVIES)
                        .build();

        public static final String TABLE_NAME = "popularmovies";
        public static final String COLUMN_MOVIE_ID = "MovieId";
        public static final String COLUMN_ORIGINAL_TITLE = "OriginalTitle";
        public static final String COLUMN_IMAGE_PATH = "ImagePath";

    }
}
