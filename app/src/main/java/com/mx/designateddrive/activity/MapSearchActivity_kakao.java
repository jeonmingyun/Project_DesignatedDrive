package com.mx.designateddrive.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import com.mx.designateddrive.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.mx.designateddrive.Constants.END_POINT;
import static com.mx.designateddrive.Constants.START_POINT;

public class MapSearchActivity_kakao extends AppCompatActivity implements View.OnClickListener, MapView.MapViewEventListener, MapView.CurrentLocationEventListener {

    int whatSearch;
    Button submitBtn, address_search_btn;
    TextView address_name, address;
    EditText address_search_text;
    ViewGroup mapViewContainer;
    MapView mapView;
    double currentLatitude, currentLongitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        submitBtn = findViewById(R.id.submit_btn);
        address_name = findViewById(R.id.address_name);
        address = findViewById(R.id.address);
        mapViewContainer = findViewById(R.id.map_view);
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
                Log.e("MapSearchAcitiv setView", "fail");
        }

    }

    private void createMapView() {
        mapView = new MapView(this);
        MapPOIItem marker = new MapPOIItem();

        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);

//        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(currentLatitude, currentLongitude), true);
        mapView.zoomIn(true);
        mapView.zoomOut(true);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(currentLatitude, currentLongitude));
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);

        mapViewContainer.addView(mapView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        currentLatitude = mapView.getMapCenterPoint().getMapPointGeoCoord().latitude;
        currentLongitude = mapView.getMapCenterPoint().getMapPointGeoCoord().longitude;
        Log.e("current_move", currentLatitude+" / "+currentLongitude);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float v) {
        currentLatitude = currentLocation.getMapPointGeoCoord().latitude;
        currentLongitude = currentLocation.getMapPointGeoCoord().longitude;
        Log.e("current", currentLatitude+" / "+currentLongitude);
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Toast.makeText(this, "현재 위치 인식 실패", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Toast.makeText(this, "현재 위치 인식 취소", Toast.LENGTH_SHORT).show();
    }


}
