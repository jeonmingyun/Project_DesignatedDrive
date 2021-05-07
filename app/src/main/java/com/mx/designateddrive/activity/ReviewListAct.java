package com.mx.designateddrive.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mx.designateddrive.R;
import com.mx.designateddrive.adapter.ReviewAdapter;
import com.mx.designateddrive.db.DBOpenHelper;
import com.mx.designateddrive.db.schema.REVIEWTABLE;
import com.mx.designateddrive.vo.ReviewVO;

import java.util.ArrayList;

public class ReviewListAct extends AppCompatActivity {

    private RecyclerView review_list;
    private TextView driver_name;
    private ArrayList<ReviewVO> dataList = new ArrayList<>();
    private ReviewAdapter adapter;
    private Button review_btn;
    private String mDriverName;
    private BroadcastReceiver mReceiver = null;
    private DBOpenHelper mdb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list_act);
        mdb = new DBOpenHelper(this);

        registerReceiver();
        driver_name = findViewById(R.id.driver_name);
        review_btn = findViewById(R.id.review_btn);
        if (getIntent().getExtras() != null) {
            mDriverName = getIntent().getStringExtra("name");
            driver_name.setText(mDriverName);
            setData();
        }

        adapter = new ReviewAdapter(this, dataList);
        review_list = findViewById(R.id.review_recycler);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        review_list.setLayoutManager(lim);
        review_list.setAdapter(adapter);

        review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewListAct.this, ReviewAct.class);
                intent.putExtra("driver_name", mDriverName);
                startActivity(intent);
            }
        });
    }

    private void setData() {
        ReviewVO item;
        Cursor cursor = mdb.getReviewList(mDriverName);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                item = new ReviewVO();
                item.driver_name = cursor.getString(cursor.getColumnIndex(REVIEWTABLE.DRIVER_NAME));
                item.review_date = cursor.getString(cursor.getColumnIndex(REVIEWTABLE.REG_DATE));
                String rating = cursor.getString(cursor.getColumnIndex(REVIEWTABLE.REVIEW_STAR));
                item.rating = Integer.parseInt(rating);
                item.user_name = cursor.getString(cursor.getColumnIndex(REVIEWTABLE.USER_NAME));
                item.review_txt = cursor.getString(cursor.getColumnIndex(REVIEWTABLE.USER_NAME));
                dataList.add(item);
            }
        }

    }


    private void registerReceiver() {
        if (mReceiver != null) return;

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction("review_add");
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ReviewVO item = (ReviewVO) new ReviewVO().fromJson(intent.getStringExtra("data"));
                dataList.add(item);
                mdb.insertReview(item);

                adapter.notifyDataSetChanged();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, theFilter);
    }

    private void unregisterReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}
