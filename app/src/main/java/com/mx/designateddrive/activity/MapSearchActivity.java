package com.mx.designateddrive.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mx.designateddrive.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import static com.mx.designateddrive.Constants.END_POINT;
import static com.mx.designateddrive.Constants.START_POINT;

public class MapSearchActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback {

    private int whatSearch;
    private EditText address_search_text;
    private TextView location_txt;
    private ImageView marker_img;
    private Button submitBtn;
    private double currentLatitude, currentLongitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        submitBtn = findViewById(R.id.submit_btn);
        address_search_text = findViewById(R.id.location_search_text);

        findViewById(R.id.submit_btn).setOnClickListener(this);
        findViewById(R.id.location_search_btn).setOnClickListener(this);

        /* 검색할 데이터에 따라 view setting */
        whatSearch = getIntent().getIntExtra("search", START_POINT);
        setView();

        createMapView();
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
            case R.id.location_search_text:
                break;
            case R.id.location_search_btn:
                String url = "kakaomap://search?q="+address_search_text.getText()+"&p="+currentLatitude+","+currentLongitude;
                Log.e("url", url);
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent2);
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
            default:
                submitBtn.setText("현재 위치로 설정");
                Log.e("MapSearchAcitiv setView", "fail");
        }

    }

    private void createMapView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
