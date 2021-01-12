package com.mx.designateddrive.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.designateddrive.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.mx.designateddrive.Constants.END_POINT;
import static com.mx.designateddrive.Constants.START_POINT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView searchStartPointText, searchEndPointText;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchStartPointText = findViewById(R.id.search_start_point_text);
        searchEndPointText = findViewById(R.id.search_end_point_text);

        findViewById(R.id.search_start_point_layout).setOnClickListener(this);
        findViewById(R.id.search_end_point_layout).setOnClickListener(this);

        // permission check
        if (ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[0])
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(REQUIRED_PERMISSIONS, 0);
            }
        }else {

        }
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

        String location = null;
        if (data != null) {
            location = data.getStringExtra("location");
        }

        if (requestCode == START_POINT) {
            if (resultCode == RESULT_OK) {
                searchStartPointText.setText(location);
            } else {

            }
        } else if (requestCode == END_POINT) {
            if (resultCode == RESULT_OK) {
                searchEndPointText.setText(location);
            } else {

            }
        }

    }

    // permission
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                Log.d("MainActivity", "start");
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
