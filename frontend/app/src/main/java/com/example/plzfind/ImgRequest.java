package com.example.plzfind;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ImgRequest {
    String postUrl="http://180.228.125.111:5000/"; //서버의 IP

    String returnValue=""; //서버로부터 반환값을 받아와 저장하는 객체(임시로 문자열로 해둠)


    protected void connectServer(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream(); //압축된 bitmap의 byte배열을 담을 스트림

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //이미지의 bitmap 압축
        byte[] byteArray=stream.toByteArray();

        RequestBody postBodyImage = new MultipartBody.Builder()
               .setType(MultipartBody.FORM)
               .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
               .build();
        postRequest(postUrl, postBodyImage);
    }//connectServer()

    //서버에 전송
    private void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        //서버에post데이터 요청
        client.newCall(request).enqueue(new Callback() {
            //이미지 전송 성공 시 서버로부터 return값 받아옴
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                ResponseBody responseBody=response.body();
                returnValue=response.body().string();
                Log.d("test_반환값",returnValue);
            }
            //이미지 전송 실패 시 동작
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("test_FailLog",e.getMessage());
            }
        });
    }//postRequest()

    //서버로부터 받은 반환값을 다시 반환해주는 메소드
    protected String getReturn(){
        return returnValue;
    }//getRequest()
}//ImgRequest class
