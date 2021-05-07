package com.mx.designateddrive.Thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mx.designateddrive.Constants;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpThread extends Thread {
    private String url = Constants.path;
    private String jsonMessage;
    private Handler handler;

    public HttpThread(String requestUrl, JSONObject jObj) {
        url += requestUrl;
        jsonMessage = jObj.toString();
    }

    @Override
    public void run() {
        try {
            Message message = Message.obtain();
            message.obj = requestPost();

            new HttpHandler().sendMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*POST 동기*/
    public String requestPost() {
        String result = "";
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("password", "post")
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonMessage)) //POST로 전달할 내용 설정
                    .build();

            //동기 처리시 execute함수 사용
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                ResponseBody body = response.body();
                if(body != null) {
                    result = body.string();
                    Log.e("response success", result);
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
