package com.mx.designateddrive.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mx.designateddrive.R;

import static com.mx.designateddrive.Constants.END_POINT;
import static com.mx.designateddrive.Constants.START_POINT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView searchStartPointText, searchEndPointText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchStartPointText = findViewById(R.id.search_start_point_text);
        searchEndPointText = findViewById(R.id.search_end_point_text);

        findViewById(R.id.search_start_point_layout).setOnClickListener(this);
        findViewById(R.id.search_end_point_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_start_point_layout:
                Intent intent_start = new Intent(this, MapSearchActivity.class);
                intent_start.putExtra("search", START_POINT);
                startActivityForResult(intent_start, START_POINT);
                break;
            case R.id.search_end_point_layout:
                Intent intent_end = new Intent(this, MapSearchActivity.class);
                intent_end.putExtra("search", END_POINT);
                startActivityForResult(intent_end, END_POINT);
                break;
            default:
                Log.e("MainActivity", "no click event");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String location = data.getStringExtra("location");

        if(requestCode == START_POINT){
            if (resultCode==RESULT_OK) {
                searchStartPointText.setText(location);
            }else{

            }
        }else if(requestCode == END_POINT){
            if (resultCode==RESULT_OK) {
                searchEndPointText.setText(location);
            }else{

            }
        }

    }
}
