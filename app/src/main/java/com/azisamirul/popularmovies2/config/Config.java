package com.azisamirul.popularmovies2.config;

import com.azisamirul.popularmovies2.BuildConfig;

/**
 * Created by azisamirul on 09/07/2017.
 */

public class Config {
    public static final String ADDRESS="http://api.themoviedb.org/3";
    public static final String API_KEY="?api_key=";
    public static final String API_KEY_VALUE= BuildConfig.MOVIE_DB_API_KEY;
    public static final String POPULAR="/movie/popular";
    public static final String TOP_RATED="/movie/top_rated";
    public static final String IMAGE_URL="http://image.tmdb.org/t/p";
    public static final String IMAGE_SIZE="/w500";
    public static final String IMAGE_SIZE_DETAIL="/w342";
    public static final String MOVIE_DETAIL="/movie/";
    public static final String TRAILER_VIDEO="&append_to_response=videos";
    public static final String YOUTUBE_TRAILER="https://www.youtube.com/watch?v=";
    public static final String isFavourite="IS_FAVOURITE";
    public static final String movieId="MOVIE_ID";
    public static final String REVIEW_CONTENT="review_content";
    public static final String REVIEW="/reviews";
    public static final String REVIEW_DETAIL="/review/";
    public static final String REVIEW_LIST_POSITION="review_position";



}
