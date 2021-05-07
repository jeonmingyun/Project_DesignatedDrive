package com.mx.designateddrive.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mx.designateddrive.R;
import com.mx.designateddrive.gps.GpsTracker;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import static com.mx.designateddrive.Constants.ADD_LAYOVER;
import static com.mx.designateddrive.Constants.END_POINT;
import static com.mx.designateddrive.Constants.START_POINT;

public class MapSearchActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback {

    private int whatSearch;
    private GoogleMap googleMap;
    private PlacesClient placesClient;
    private TextView locationText;
    private ImageView markerImg;
    private Button submitBtn;
    private GpsTracker gpsTracker;
    private double currentLatitude = 0, currentLongitude = 0;

    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        submitBtn = findViewById(R.id.submit_btn);
        locationText = findViewById(R.id.location_text);
        markerImg = findViewById(R.id.marker);

        findViewById(R.id.submit_btn).setOnClickListener(this);// Initialize the SDK

        /* 검색할 데이터에 따라 view setting (Activity 재활용)*/
        whatSearch = getIntent().getIntExtra("search", START_POINT);
        setView();

        gpsTracker = new GpsTracker(this);
        currentLatitude = gpsTracker.getLatitude(); // 위도
        currentLongitude = gpsTracker.getLongitude(); //경도

        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_map_api_key));
        placesClient = Places.createClient(this);
        createMapView();
        autoCompleteSearch();
    }

    public void createMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                Intent intent = new Intent();
                intent.putExtra("location", locationText.getText());
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                Log.e("MapSearchActivity", "no click event");
        }
    }

    public void autoCompleteSearch() {

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                String address = place.getAddress();
                String id = place.getId();
                String name = place.getName();

                googleMap.clear();
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                locationText.setText(address);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("MapSearchActivity", "An error occurred: " + status);
            }
        });

    }

    public void setView() {
        switch (whatSearch) {
            case START_POINT:
                submitBtn.setText("출발지로 설정");
                break;
            case END_POINT:
                submitBtn.setText("도착지로 설정");
                break;
            case ADD_LAYOVER:
                submitBtn.setText("경유지 추가");
                break;
            default:
                submitBtn.setText("현재 위치로 설정");
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                new LatLng(currentLatitude, currentLongitude)   // 위도, 경도
        ));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);
        this.googleMap.animateCamera(zoom);
        // marker 표시
        // market 의 위치, 타이틀, 짧은설명 추가 가능.
        final MarkerOptions marker = new MarkerOptions();

        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
//                BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.targeticon);
//                Bitmap b = bitmapDrawable.getBitmap();
//                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 170, false);
//                marker.position(latLng).title("여기").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
//                googleMap.addMarker(marker).showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                markerImg.setVisibility(View.VISIBLE);
            }
        });

        this.googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                locationText.setText("위치 이동 중");
                /*markerImg.setVisibility(View.GONE);*/
            }
        });

        this.googleMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {
                Log.d("on", "A");
            }
        });

        this.googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

            }
        });

        this.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                String address = gpsTracker.getCurrentAddress(MapSearchActivity.this, googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                locationText.setText(address);
            }
        });

    }

}
