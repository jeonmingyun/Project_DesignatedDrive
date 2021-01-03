package com.mx.designateddrive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mx.designateddrive.R;

import static com.mx.designateddrive.Constants.END_POINT;
import static com.mx.designateddrive.Constants.START_POINT;

public class MapSearchActivity extends AppCompatActivity implements View.OnClickListener{

    int whatSearch;
    Button submitBtn;
    TextView address_name, address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        submitBtn = findViewById(R.id.submit_btn);
        address_name = findViewById(R.id.address_name);
        address = findViewById(R.id.address);

        findViewById(R.id.submit_btn).setOnClickListener(this);

        /* 검색할 데이터에 따라 view setting */
        whatSearch = getIntent().getIntExtra("search", START_POINT);
        setView();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                Intent intent = new Intent();
                intent.putExtra("location", address.getText());
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                Log.e("MapSearchActivity", "no click event");
        }
    }

    private void setView() {
        switch (whatSearch) {
            case START_POINT:
                submitBtn.setText("출발지로 설정");
                break;
            case END_POINT:
                submitBtn.setText("도착지로 설정");
                break;

        }

    }
}
