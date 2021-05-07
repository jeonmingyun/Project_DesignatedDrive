package com.mx.designateddrive.Thread;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

public class HttpHandler extends Handler {
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        try {
            JSONObject JsonMessage = new JSONObject(msg.obj.toString());
            String name = JsonMessage.getString("name");
            String comment = JsonMessage.getString("comment");

            Log.e("handler", JsonMessage.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
