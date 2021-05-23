package com.example.plzfind;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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

public class ImgRequest {
    private static String imageUrl="http://180.228.125.111:5000/image"; //서버의 IP
    private static String nameUrl="http://180.228.125.111:5000/name"; //서버의 IP

    private static Bitmap returnImgBitmap=null; //서버로부터 반환 받은 이미지를 저장할 비트맵 객체
    private static String[] retrunStr=null; //서버에서 이미지에서 판별한 상품들의 문자열을 받아 저장하는 배열

    protected static void connectServer(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream(); //압축된 bitmap의 byte배열을 담을 스트림

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //이미지의 bitmap 압축
        byte[] byteArray=stream.toByteArray();

        RequestBody postBodyImage = new MultipartBody.Builder()
               .setType(MultipartBody.FORM)
               .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
               .build();
        postRequest(imageUrl, postBodyImage);
    }//connectServer()

    //서버에 전송
    private static void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient imgClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        //서버에post데이터 요청
        imgClient.newCall(request).enqueue(new Callback() {
            //이미지 전송 성공 시 서버로부터 return값 받아옴
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if(response.isSuccessful()){
                    //서버로부터 받은 값을 bitmap으로 바꿈
                    returnImgBitmap=BitmapFactory.decodeStream(response.body().byteStream());
                    Log.d("test_반환값", String.valueOf(returnImgBitmap.getByteCount()));

                    //이미지가 post로 서버에 전송되어 box처리가된 이미지가 성공적으로 도착하면 이미지에서 식별한 상품들의 이름을 얻기 위한 get메소드 사용
                    OkHttpClient strClient=new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(nameUrl)
                            .build();
                    strClient.newCall(request).enqueue(new Callback(){

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            try {
                                JSONObject jsonObject=new JSONObject(response.body().string()); //서버로부터 이미지에서 판별한 상품 이름들을 json으로 받아옴
                                retrunStr=new String[jsonObject.length()];
                                for(int i=0;i<jsonObject.length();i++){
                                    retrunStr[i]=jsonObject.getString("productName"+i);
                                    Log.d("test_상품이름"+i,retrunStr[i]);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }
                    });
                }
            }
            //이미지 전송 실패 시 동작
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("test_FailLog",e.getMessage());
            }
        });
    }//postRequest()

    //서버로부터 받은 반환값(이미지)을 다시 반환해주는 메소드
    protected static Bitmap getBitmap(){
        return returnImgBitmap;
    }//getRequest()
}//ImgRequest class
