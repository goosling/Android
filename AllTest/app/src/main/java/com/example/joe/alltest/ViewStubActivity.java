package com.example.joe.alltest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

/**
 * Created by JOE on 2016/2/28.
 */
public class ViewStubActivity extends Activity{

    private Button btn1, btn2, btn3;
    private ViewStub viewStub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        viewStub = (ViewStub)findViewById(R.id.stub);
        viewStub.setOnInflateListener(inflateListener);
        btn1.setOnClickListener(click);
        btn2.setOnClickListener(click);
        btn3.setOnClickListener(click);
    }

    private ViewStub.OnInflateListener inflateListener = new ViewStub.OnInflateListener() {
        @Override
        public void onInflate(ViewStub stub, View inflated) {
            Toast.makeText(ViewStubActivity.this, "ViewStub is loaded",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn1:
                    try{
                        LinearLayout layout = (LinearLayout)viewStub.inflate();
                        RatingBar rating = (RatingBar)findViewById(R.id.ratingBar);
                        rating.setNumStars(5);
                    }catch(Exception e) {
                        viewStub.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn2:
                    viewStub.setVisibility(View.GONE);
                    break;
                case R.id.btn3:
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.inflateStart);
                    RatingBar rating = (RatingBar)findViewById(R.id.ratingBar);
                    float ratingNum = rating.getRating();
                    ratingNum++;
                    if(ratingNum>4) {
                        ratingNum = 0;
                    }
                    rating.setRating(ratingNum);
                    break;
            }
        }
    };
}
