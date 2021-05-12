package com.example.plzfind;

import android.Manifest;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView imgV;
    Button Bt1;
    Button bt_gallery;
    Button bt_send;
    Uri imgUri;
    int img_size;

    ImgRequest imgRequest=null; //이미지 파일 업로드하기 위한 객체
    Bitmap bitmap=null; //이미지 선택 및 촬영 후 이미지를 비트맵으로 저장하기 위한 객체

    Bitmap get_bitmap = null; // 받아온 이미지 객체

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
        bt_send=findViewById(R.id.bt_send);

        imgRequest=new ImgRequest(); //이미지 파일 업로드하기 위한 객체 생성

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //권한설정 ( 마시멜로우 이상 Version)
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    ){
            }else
            {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
        //화면의 '전송'버튼 선택 시 동작
        bt_send.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //bitmap에 이미지가 들어 있을 경우에만 전송관련 기능 호출출
               if(bitmap!=null){
                   imgRequest.connectServer(bitmap);
                   String pro_name = "G304"; // 제품명 임시

                   get_bitmap = bitmap; // 여기에 받아온 비트맵 이미지 넣으면 될듯 _동건(임시)
                   ByteArrayOutputStream stream = new ByteArrayOutputStream();

                   img_size = get_bitmap.getByteCount();
                   if(img_size >= 5242880){
                       get_bitmap.compress(Bitmap.CompressFormat.JPEG,10,stream);
                   }
                   else if(img_size >= 4194304 || img_size <= 5242879){
                       get_bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
                   }
                   else if(img_size >= 3145728 || img_size <= 4194303){
                       get_bitmap.compress(Bitmap.CompressFormat.JPEG,60,stream);
                   }
                   else if(img_size >= 0 || img_size <= 3145727){
                       get_bitmap.compress(Bitmap.CompressFormat.JPEG,70,stream);
                   }
                   else{
                       Toast.makeText(getApplicationContext(),"이미지선택 오류.",Toast.LENGTH_SHORT).show();
                   }
                   byte[] byteArrayBitmap = stream.toByteArray();

                   Intent intent = new Intent(MainActivity.this, get_Img_Data.class);
                   intent.putExtra("pro_name",pro_name);
                   intent.putExtra("pro_image",byteArrayBitmap);
                   startActivity(intent);

                }else if(bitmap==null){
                    Toast.makeText(getApplicationContext(),"이미지를 선택 또는 촬영해 주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//onCreate()

    //이미지 비트맵 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //카메라로 사진 촬영 시
        if(requestCode == TAKE_CAMERA){
            if(resultCode==RESULT_OK){
                if(data!=null){
                    bitmap= (Bitmap)data.getExtras().get("data");
                    imgV.setImageBitmap(bitmap);
                }
            }
        }
        //앨범 에서 선택 시
        else if(requestCode == PICK_FROM_ALBUM){
            if(resultCode==RESULT_OK){
                imgUri = data.getData();
                try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                        imgV.setImageBitmap(bitmap);
                } catch(IOException e){

                }
            }
        }
    }//onActivityResult()
}
