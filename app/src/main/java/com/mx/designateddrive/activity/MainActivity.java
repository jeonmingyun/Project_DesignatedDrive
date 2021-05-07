package com.mx.designateddrive.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mx.designateddrive.R;
import com.mx.designateddrive.Thread.HttpThread;
import com.mx.designateddrive.db.DBOpenHelper;
import com.mx.designateddrive.gps.GpsTracker;
import com.mx.designateddrive.vo.CurrentCallVO;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import static com.mx.designateddrive.Constants.ADD_LAYOVER;
import static com.mx.designateddrive.Constants.END_POINT;
import static com.mx.designateddrive.Constants.START_POINT;

public class MainActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

    private double currentLatitude, currentLongitude;
    private String currentAddress;
    private TextView searchStartPointText, searchEndPointText;
    private String layover, userPhoneNum;
    private GpsTracker gpsTracker;
    private DBOpenHelper mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mdb = new DBOpenHelper(this);

        searchStartPointText = findViewById(R.id.search_start_point_text);
        searchEndPointText = findViewById(R.id.search_end_point_text);

        findViewById(R.id.search_start_point_layout).setOnClickListener(this);
        findViewById(R.id.search_end_point_layout).setOnClickListener(this);
        findViewById(R.id.add_layover_btn).setOnClickListener(this);
        findViewById(R.id.call_btn).setOnClickListener(this);
        findViewById(R.id.pro_btn).setOnClickListener(this);
        findViewById(R.id.go_review).setOnClickListener(this);
//        findViewById(R.id.reviewList).setOnClickListener(this);

        permissionCheck();
        getPhoneNum();

        FirebaseApp.initializeApp(this);
        String regId = FirebaseInstanceId.getInstance().getToken();
        if (regId != null) {
            Log.d("on", regId);
        }

        gpsTracker = new GpsTracker(MainActivity.this);
        currentLatitude = gpsTracker.getLatitude(); // 위도
        currentLongitude = gpsTracker.getLongitude(); //경도
        currentAddress = gpsTracker.getCurrentAddress(MainActivity.this, currentLatitude, currentLongitude);
        searchStartPointText.setText(currentAddress);

        createMapView();

        /* Http 통신 test */
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("name", "짱구");
            jObj.put("comment", "android request");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpThread("/api/sendData", jObj).start();
    }

    private void getPhoneNum() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(REQUIRED_PERMISSIONS, 0);
            }
        }

        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        userPhoneNum = telManager.getLine1Number();

        if (userPhoneNum.startsWith("+82")) {
            userPhoneNum = userPhoneNum.replace("+82", "0");
        }
    }

    private void permissionCheck() {

        boolean permissionCheck = true;

        for (String permissionStr : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permissionStr)
                    != PackageManager.PERMISSION_GRANTED) {

                permissionCheck = false;

            }
        }

        if (!permissionCheck) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(REQUIRED_PERMISSIONS, 0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_start_point_layout:
                Intent intentStart = new Intent(this, MapSearchActivity.class);
                intentStart.putExtra("search", START_POINT);
                startActivityForResult(intentStart, START_POINT);
                break;
            case R.id.search_end_point_layout:
                Intent intentEnd = new Intent(this, MapSearchActivity.class);
                intentEnd.putExtra("search", END_POINT);
                startActivityForResult(intentEnd, END_POINT);
                break;
            case R.id.add_layover_btn:
                Intent intentAddLayover = new Intent(this, MapSearchActivity.class);
                intentAddLayover.putExtra("search", ADD_LAYOVER);
                startActivityForResult(intentAddLayover, ADD_LAYOVER);
                break;
            case R.id.call_btn:
                Toast.makeText(this, "호출 되었습니다.", Toast.LENGTH_SHORT).show();
                insertCurrentCall();
                break;
            case R.id.pro_btn:
                Intent intent = new Intent(MainActivity.this, ProfileAct.class);
                startActivity(intent);
                break;
            case R.id.go_review:
//                Intent reviewIntent = new Intent(MainActivity.this, ReviewAct.class);
                Intent reviewIntent = new Intent(MainActivity.this, ReviewProAct.class);
                startActivity(reviewIntent);
                break;
            default:
                Log.e("MainActivity", "no click event");

        }

    }

    private void insertCurrentCall() {
        CurrentCallVO callVO = new CurrentCallVO();
        callVO.setPhoneNumber(userPhoneNum);
        callVO.setUserName("");
        callVO.setStartPoint(searchStartPointText.getText().toString());
        callVO.setEndPoint(searchEndPointText.getText().toString());
        callVO.setLayover(layover);

        if (mdb.insertCurrentCall(callVO)) {
            Toast.makeText(this, "호출 되었습니다.", Toast.LENGTH_SHORT).show();
            Log.e("insert call", "succesful");
        } else {
            Log.e("insert call", "fail");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String location = null;
        if (data != null) {
            location = data.getStringExtra("location"); // MapSearchActivity에서 선택한 위치 정보
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
        } else if (requestCode == ADD_LAYOVER) {
            if (resultCode == RESULT_OK) {
                layover = location;
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

    public void createMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLatitude, currentLongitude))); // 위도, 경도
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);
        googleMap.animateCamera(zoom);
    }

}
