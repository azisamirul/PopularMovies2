package com.azisamirul.popularmovies2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.azisamirul.popularmovies2.adapter.ReviewListAdapter;
import com.azisamirul.popularmovies2.adapter.VideoListAdapter;
import com.azisamirul.popularmovies2.config.Config;
import com.azisamirul.popularmovies2.data.MovieContract;
import com.azisamirul.popularmovies2.data.ReviewList;
import com.azisamirul.popularmovies2.data.VideoList;
import com.azisamirul.popularmovies2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetails extends AppCompatActivity implements VideoListAdapter.VideoListOnClickHandler,ReviewListAdapter.ReviewListOnClickHandler {
    private String movie_id,
            responseData,
            title,
            year,
            duration,
            rating,
            image,
            overview,
            key,
            name,
            site,
            type;
    private boolean isFavourite;
    private TextView detailMovieTitle,
            detailMovieYear,
            detailMovieDuration,
            detailMovieRating,
            detailMovieOverview;
    private ImageView detailMovieImage;
    private Button btn_mark_as_fav;
    private ScrollView scrollView;
    private List<VideoList> mVideoList;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewReview;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManagerReview;
    private VideoListAdapter videoListAdapter;
    private VideoList videoList;

    private ReviewList reviewList;
    private List<ReviewList> mReviewList;
    private ReviewListAdapter reviewListAdapter;

    private String SCROLL_POS_KEY = "scroll_position";

    public static int posX = 0;
    public static int posY = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        scrollView = (ScrollView) findViewById(R.id.scv_detail);
        detailMovieTitle = (TextView) findViewById(R.id.tv_detail_movie_title);
        detailMovieImage = (ImageView) findViewById(R.id.img_movie_detail);
        detailMovieYear = (TextView) findViewById(R.id.tv_detail_movie_year);
        detailMovieDuration = (TextView) findViewById(R.id.tv_detail_movie_duration);
        detailMovieRating = (TextView) findViewById(R.id.tv_detail_movie_rating);
        detailMovieOverview = (TextView) findViewById(R.id.tv_detail_movie_overview);
        btn_mark_as_fav = (Button) findViewById(R.id.btn_mark_as_fav);


        mVideoList = new ArrayList<VideoList>();
        mReviewList=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_trailer_movies);
        recyclerViewReview=(RecyclerView) findViewById(R.id.rv_review_movies);

        linearLayoutManager = new LinearLayoutManager(MovieDetails.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        linearLayoutManagerReview=new LinearLayoutManager(MovieDetails.this);
        recyclerViewReview.setLayoutManager(linearLayoutManagerReview);
        recyclerViewReview.setHasFixedSize(true);

        Intent intentDetail = getIntent();
        if (intentDetail != null) {
            if (intentDetail.hasExtra(Config.movieId)) {
                movie_id = intentDetail.getStringExtra(Config.movieId);
                get_movie_detail(movie_id);
                getReview(movie_id);
            }
        }
        checkFavourite();
        btn_mark_as_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavourite) {
                    removeFromFavourite();
                } else {
                    addToFavourite();
                }
            }
        });

    }

    ///Reference from : https://asishinwp.wordpress.com/2013/04/15/save-scrollview-position-resume-scrollview-from-that-position/
    @Override
    protected void onPause() {
        super.onPause();
        posX = scrollView.getScrollX();
        posY = scrollView.getScrollY();
        Log.d("OnPause","onPause");
        Log.d("posX",String.valueOf(posX));
        Log.d("posY",String.valueOf(posY));
    }

    @Override
    protected void onResume() {
        super.onResume();

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(posX, posY);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(SCROLL_POS_KEY, new int[]{scrollView.getScrollX(), scrollView.getScrollY()});

        Log.d("Scroll x saved : ", String.valueOf(scrollView.getScrollX()));
        Log.d("Scroll y saved : ", String.valueOf(scrollView.getScrollY()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final int[] pos=savedInstanceState.getIntArray(SCROLL_POS_KEY);
        if(pos!=null){
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(pos[0],pos[1]);
                    Log.d("Scroll restore",String.valueOf(pos[1]));
                }
            });
        }
    }

    private void get_movie_detail(String movie_id) {
        if (NetworkUtils.checkInternetConnection(this)) {
            String url = Config.ADDRESS + Config.MOVIE_DETAIL + movie_id + Config.API_KEY + Config.API_KEY_VALUE + Config.TRAILER_VIDEO;
            Log.d("Video", url);

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String msg = "Data not found!";
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    toast.show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        responseData = response.body().string();

                        MovieDetails.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    title = jsonObject.getString("original_title");
                                    image = jsonObject.getString("poster_path");
                                    overview = jsonObject.getString("overview");
                                    duration = String.valueOf(jsonObject.getInt("runtime"));
                                    rating = String.valueOf(jsonObject.getDouble("vote_average"));
                                    String releaseDate = jsonObject.getString("release_date");
                                    year = releaseDate.substring(0, 4);

                                    JSONObject videos = jsonObject.getJSONObject("videos");
                                    JSONArray videoResults = videos.getJSONArray("results");

                                    for (int i = 0; i < videoResults.length(); i++) {
                                        JSONObject v = videoResults.getJSONObject(i);
                                        videoList = new VideoList();
                                        key = v.getString("key");
                                        name = v.getString("name");
                                        site = v.getString("site");
                                        type = v.getString("type");
                                        Log.d("Video", key);
                                        Log.d("Name", name);

                                        videoList.setKey(key);
                                        videoList.setName(name);
                                        videoList.setSite(site);
                                        videoList.setType(type);

                                        mVideoList.add(videoList);
                                    }

                                    videoListAdapter = new VideoListAdapter(MovieDetails.this, MovieDetails.this);
                                    recyclerView.setAdapter(videoListAdapter);
                                    videoListAdapter.addVideo(mVideoList);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                display();
                            }
                        });

                    }
                }
            });


        } else {
            String msg = "No Internet Connection";
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.show();
        }

    }


    /////GetReview
    private void getReview(final String MovId) {
        if (NetworkUtils.checkInternetConnection(this)) {
            //http://api.themoviedb.org/3/movie/83542/reviews?api_key=b97c9b657e3dd0ddad9a3b89603222ac
            //http://api.themoviedb.org/3/review/51910979760ee320eb020fc2?api_key=b97c9b657e3dd0ddad9a3b89603222ac
            String address = Config.ADDRESS + Config.MOVIE_DETAIL + MovId + Config.REVIEW + Config.API_KEY + Config.API_KEY_VALUE;
            Log.d("address review", address);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(address).get().build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String responseString = response.body().string();

                    MovieDetails.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(responseString);
                                JSONArray jsonArray = jsonObject.getJSONArray("results");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    reviewList=new ReviewList();
                                    JSONObject results = jsonArray.getJSONObject(i);
                                    String id=results.getString("id");
                                    String author = results.getString("author");
                                    String content = results.getString("content");
                                    Log.d("author ", author);
                                    reviewList.setAuthor(author);
                                    reviewList.setContent(content);
                                    reviewList.setMovieTitle(title);
                                    reviewList.setId(id);
                                    mReviewList.add(reviewList);
                                }

                                reviewListAdapter=new ReviewListAdapter(MovieDetails.this);
                                recyclerViewReview.setAdapter(reviewListAdapter);
                                reviewListAdapter.setReviewDataList(mReviewList);
                            } catch (JSONException e) {
                                Log.d("error", e.toString());
                            }
                        }
                    });
                }
            });

        } else {
            showErrorConnection();
        }
    }


    private void display() {
        String image_url = Config.IMAGE_URL + Config.IMAGE_SIZE_DETAIL + image;
        detailMovieTitle.setText(title);
        detailMovieOverview.setText(overview);
        detailMovieDuration.setText(duration + " min.");
        detailMovieRating.setText(rating);
        detailMovieYear.setText(year);
        Picasso.with(getApplicationContext()).load(image_url).into(detailMovieImage);
    }


    private void addToFavourite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie_id);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE_PATH, image);

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Toast.makeText(this, "Added to Favourite", Toast.LENGTH_LONG);
            isFavourite = true;
            changeButton();
        }

    }

    private void removeFromFavourite() {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movie_id).build();
        int deleteStatus = getContentResolver().delete(uri, null, null);

        if (deleteStatus > 0) {
            Toast.makeText(this, "Removed from Favourite", Toast.LENGTH_LONG).show();
            isFavourite = false;
            changeButton();
        } else {
            Toast.makeText(this, "Cannot Delete from Favourite Lists", Toast.LENGTH_LONG).show();
        }

    }

    private void changeButton() {
        if (isFavourite) {
            btn_mark_as_fav.setText(getString(R.string.remove_favourite));
        } else {
            btn_mark_as_fav.setText(getString(R.string.btn_detail_favourite));
        }
    }

    public void checkFavourite() {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movie_id).build();
        Log.d("Uri ", uri.toString());

        Cursor c = getContentResolver().query(uri, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{movie_id}, MovieContract.MovieEntry._ID);
        String counter = String.valueOf(c.getCount());
        Log.d("Counter", counter);
        if (c.getCount() > 0) {

            isFavourite = true;
            Log.d("status", "true");
        } else {
            isFavourite = false;
            Log.d("status", "false");
        }
        changeButton();
    }

    @Override
    public void onClick(int pos) {

    }

    private void showErrorConnection() {
        String msg = "No Internet Connection";
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }


}
