package com.mx.designateddrive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mx.designateddrive.R;

public class ReviewProAct extends AppCompatActivity implements View.OnClickListener {

    private Button park_btn, lee_btn, kim_btn;
    private String mDrivername;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverlist_act);

        park_btn = findViewById(R.id.park_btn);
        lee_btn = findViewById(R.id.lee_btn);
        kim_btn = findViewById(R.id.kim_btn);

        park_btn.setOnClickListener(this);
        lee_btn.setOnClickListener(this);
        kim_btn.setOnClickListener(this);

        intent = new Intent(ReviewProAct.this, ReviewListAct.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.park_btn:
                mDrivername = park_btn.getText().toString();
                intent.putExtra("name", mDrivername);
                startActivity(intent);
                break;
            case R.id.lee_btn:
                mDrivername = lee_btn.getText().toString();
                intent.putExtra("name", mDrivername);
                startActivity(intent);
                break;
            case R.id.kim_btn:
                mDrivername = kim_btn.getText().toString();
                intent.putExtra("name", mDrivername);
                startActivity(intent);
                break;
        }
    }
}
