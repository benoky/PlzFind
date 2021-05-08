package com.example.plzfind;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImgRequest {
    String postUrl="http://112.156.72.74:5000/"; //서버의 IP

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
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {

            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }//postRequest()


}
