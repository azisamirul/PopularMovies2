package com.azisamirul.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.azisamirul.popularmovies2.config.Config;
import com.azisamirul.popularmovies2.data.ReviewList;
import java.util.ArrayList;

public class ReviewDetails extends AppCompatActivity {
    private TextView reviewDetailAuthor;
    private TextView reviewDetailContent;
    private TextView reviewDetailTitle;
    private ScrollView scrollViewReview;
    private ArrayList<ReviewList> reviewList;

    private int position;
    private Intent i;

    private static int posX = 0;
    private static int posY = -1;
    private String SCROLL_SAVE_POS="scroll_pos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);
        reviewDetailAuthor = (TextView) findViewById(R.id.tv_review_detail_author);
        reviewDetailContent = (TextView) findViewById(R.id.tv_review_detail_content);
        reviewDetailTitle = (TextView) findViewById(R.id.tv_review_detail_title);
        scrollViewReview = (ScrollView) findViewById(R.id.scrollViewReview);
        i = getIntent();
        if (i.getParcelableArrayListExtra(Config.REVIEW_CONTENT) != null) {
            if (i.hasExtra(Config.REVIEW_LIST_POSITION)) {
                position = i.getIntExtra(Config.REVIEW_LIST_POSITION, 0);
            }
            reviewList = i.getParcelableArrayListExtra(Config.REVIEW_CONTENT);
            parseParcel();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        posX=scrollViewReview.getScrollX();
        posY=scrollViewReview.getScrollY();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollViewReview.post(new Runnable() {
            @Override
            public void run() {
                scrollViewReview.scrollTo(posX,posY);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(SCROLL_SAVE_POS,new int[]{scrollViewReview.getScrollX(),scrollViewReview.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] pos=savedInstanceState.getIntArray(SCROLL_SAVE_POS);
        if(pos!=null) {
            scrollViewReview.scrollTo(pos[0],pos[1]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle("Movie Review");
        return true;
    }

    private void parseParcel() {
        if (reviewList.size() != 0) {
            reviewDetailTitle.setText(reviewList.get(position).getMovieTitle());
            reviewDetailAuthor.setText(reviewList.get(position).getAuthor());
            reviewDetailContent.setText(reviewList.get(position).getContent());
        } else {
            Toast.makeText(this, "Review Content not Available", Toast.LENGTH_LONG).show();
        }
    }

}
