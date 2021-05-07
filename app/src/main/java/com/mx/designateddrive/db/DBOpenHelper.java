package com.mx.designateddrive.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mx.designateddrive.db.schema.CURRENT_CALL_TABLE;
import com.mx.designateddrive.db.schema.REVIEWTABLE;
import com.mx.designateddrive.vo.CurrentCallVO;
import com.mx.designateddrive.vo.ReviewVO;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "driveVer1.db";
    public static SQLiteDatabase db;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(REVIEWTABLE.CREATE_QUERY);
        db.execSQL(CURRENT_CALL_TABLE.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(REVIEWTABLE.DROP_QUERY);
        db.execSQL(CURRENT_CALL_TABLE.DROP_QUERY);
        onCreate(db);
    }

    public boolean insertReview(ReviewVO item) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REVIEWTABLE.DRIVER_NAME, item.driver_name);
        values.put(REVIEWTABLE.REG_DATE, item.review_date);
        values.put(REVIEWTABLE.REVIEW_STAR, item.rating);
        values.put(REVIEWTABLE.USER_NAME, item.user_name);
        values.put(REVIEWTABLE.REVIEW, item.review_txt);
        long result = db.insert(REVIEWTABLE.TABLENAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getReviewList(String driver_name) {
        Log.d("on", driver_name);
        db = this.getReadableDatabase();
        Cursor list = db.rawQuery("select * from reviewTable where driver_name = ?", new String[]{driver_name});
        return list;
    }

    public boolean insertCurrentCall(CurrentCallVO callVO) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CURRENT_CALL_TABLE.PHONE_NUMBER, callVO.getPhoneNumber());
        values.put(CURRENT_CALL_TABLE.USER_NAME, callVO.getUserName());
        values.put(CURRENT_CALL_TABLE.START_POINT, callVO.getStartPoint());
        values.put(CURRENT_CALL_TABLE.END_POINT, callVO.getEndPoint());
        values.put(CURRENT_CALL_TABLE.LAYOVER, callVO.getLayover());

        long result = db.insert(CURRENT_CALL_TABLE.TABLENAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}

