package com.azisamirul.popularmovies2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.azisamirul.popularmovies2.adapter.PopularMoviesAdapter;
import com.azisamirul.popularmovies2.config.Config;
import com.azisamirul.popularmovies2.data.MovieData;
import com.azisamirul.popularmovies2.utilities.NetworkUtils;

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

public class MainActivity extends AppCompatActivity implements PopularMoviesAdapter.PopularMoviesOnClickHandler {
    private final String SHOW_POPULAR_KEYWORD = "popular";
    private final String SHOW_TOP_RATED_KEYWORD = "top_rated";
    private RecyclerView recyclerView;
    private PopularMoviesAdapter popularMoviesAdapter;
    String responseData;
    private GridLayoutManager gridLayoutManager;
    public List<MovieData> movieDataList;
    private MovieData movieData;
    Parcelable parcelable;
    private String MOVIE_LIST_SAVED = "movie_list_saved";
    private String GRID_SAVED = "grid_saved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieDataList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_popular_movies);

//References from :https://stackoverflow.com/questions/29579811/changing-number-of-columns-with-gridlayoutmanager-and-recyclerview/29582477#29582477
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        } else {
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIE_LIST_SAVED)) {
                if (savedInstanceState.containsKey(GRID_SAVED)) {
                    parcelable = savedInstanceState.getParcelable(GRID_SAVED);
                }
                gridLayoutManager.onRestoreInstanceState(parcelable);
                movieDataList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_SAVED);
                popularMoviesAdapter = new PopularMoviesAdapter(MainActivity.this, getApplicationContext());
                recyclerView.setAdapter(popularMoviesAdapter);
                popularMoviesAdapter.setMovieDataList(movieDataList);
            }
        } else {
            callService(SHOW_POPULAR_KEYWORD);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_popular:
                callService(SHOW_POPULAR_KEYWORD);
                return true;
            case R.id.action_top_rated:
                callService(SHOW_TOP_RATED_KEYWORD);
                return true;
            case R.id.action_favourite:
                Intent intent = new Intent(this, FavouritesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callService(String sortType) {
        if (NetworkUtils.checkInternetConnection(this)) {
            String address = "";
            String popular_movie_address = Config.ADDRESS + Config.POPULAR + Config.API_KEY + Config.API_KEY_VALUE;
            String popular_toprated_address = Config.ADDRESS + Config.TOP_RATED + Config.API_KEY + Config.API_KEY_VALUE;
            address = popular_movie_address;
            switch (sortType) {
                case SHOW_POPULAR_KEYWORD: {
                    address = popular_movie_address;
                    Log.d("ADDRESS POPULAR", address);
                    break;
                }
                case SHOW_TOP_RATED_KEYWORD: {
                    address = popular_toprated_address;
                    Log.d("ADDRESS TOP", address);
                    break;
                }
            }

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(address)
                    .get()
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("Fail", "fail");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    responseData = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("Response data", responseData);
                                JSONObject jsonObject = new JSONObject(responseData);
                                JSONArray jsonArray = jsonObject.getJSONArray("results");
                                clearList();


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    movieData = new MovieData();
                                    JSONObject resultObject = jsonArray.getJSONObject(i);
                                    String movie_id = String.valueOf(resultObject.getInt("id"));
                                    String original_title = resultObject.getString("original_title");
                                    String poster_path = resultObject.getString("poster_path");
                                    Log.d("Original title", original_title);
                                    Log.d("poster_path", poster_path);
                                    movieData.setId(movie_id);
                                    movieData.setOriginalTitle(original_title);
                                    movieData.setPosterPath(poster_path);
                                    movieDataList.add(movieData);
                                }
                                popularMoviesAdapter = new PopularMoviesAdapter(MainActivity.this, getApplicationContext());
                                recyclerView.setAdapter(popularMoviesAdapter);
                                popularMoviesAdapter.setMovieDataList(movieDataList);
                                popularMoviesAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {

                            }
                        }
                    });
                }
            });

        } else {
            showErrorConnection();
        }

    }

    private void showErrorConnection() {
        String msg = "No Internet Connection";
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable parcelables = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(GRID_SAVED, parcelables);
        outState.putParcelableArrayList(MOVIE_LIST_SAVED, new ArrayList<MovieData>(movieDataList));
    }

    private void clearList() {
        if (movieDataList != null && !movieDataList.isEmpty()) {
            movieDataList.clear();
            popularMoviesAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(int clickedItemIndex) {

    }
}
