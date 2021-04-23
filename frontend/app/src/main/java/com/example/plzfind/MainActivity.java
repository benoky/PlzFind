package com.example.plzfind;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import android.Manifest;
import androidx.core.app.ActivityCompat;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView imgV;
    Button Bt1;
    Button bt_gallery;
    Uri imgUri;

    static final int TAKE_CAMERA=1;
    static final int PICK_FROM_ALBUM = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        imgV = findViewById(R.id.ImgView1);
        Bt1 =findViewById(R.id.Bt_camara);
        bt_gallery = findViewById(R.id.Bt_gallery);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //권한설정 ( 마시멜로우 이상 Version)
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
            }else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , 1);
            }
        }

        Bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                //내부 카메라 사진
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                //2번 화면 전환
                startActivityForResult(intent,TAKE_CAMERA);
            }
        });
        bt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
    }


    //3번 카메라로 찍은 사진 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_CAMERA){
            if(resultCode==RESULT_OK){
                if(data!=null){
                    Bitmap b= (Bitmap)data.getExtras().get("data");
                    imgV.setImageBitmap(b);
                }
            }
        }
        else if(requestCode == PICK_FROM_ALBUM){
            if(resultCode==RESULT_OK){
                imgUri = data.getData();
                try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                        imgV.setImageBitmap(bitmap);
                } catch(IOException e){

                }
            }
        }
    }
}
