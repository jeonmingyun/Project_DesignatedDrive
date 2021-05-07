package com.mx.designateddrive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mx.designateddrive.R;
import com.mx.designateddrive.vo.ReviewVO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReviewAct extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button review_btn;
    private TextView title;
    private EditText review_et;
    private Date currentTime = Calendar.getInstance().getTime();
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_act);

        title = findViewById(R.id.title);
        ratingBar = findViewById(R.id.star_bar);
        review_btn = findViewById(R.id.review_btn);
        review_et = findViewById(R.id.review_et);
        if(getIntent().getExtras() != null){
            title.setText(getIntent().getStringExtra("driver_name"));
        }

        review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewVO item = new ReviewVO();
                item.rating = (int)ratingBar.getRating();
                item.review_txt = review_et.getText().toString();
                item.user_name = "테스트";
                item.review_date = formatter.format(currentTime);
                item.driver_name = title.getText().toString();
                Intent intent = new Intent("review_add");
                intent.putExtra("data", item.toJson());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                finish();
            }
        });
    }
}
