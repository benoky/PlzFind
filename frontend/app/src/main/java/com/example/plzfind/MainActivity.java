package com.example.plzfind;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ImageView imgV;
    Button Bt1;
    Button bt_gallery;
    Button bt_send;
    Uri imgUri;
    File file;

    Bitmap sendBitmap=null; //이미지 선택 및 촬영 후 이미지를 비트맵으로 저장하기 위한 객체
    String currentPhotoPath;

    final static int TAKE_PICTURE = 1;
    final static int REQUEST_TAKE_PHOTO = 1;
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

        sendBitmap = null;
        currentPhotoPath = null;

        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard,"capture.jpg");

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
                dispatchTakePictureIntent();
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
            public void onClick(View v) {
                //bitmap에 이미지가 들어 있을 경우에만 전송관련 기능 호출출
               if(sendBitmap!=null){
                   Bitmap requestBitmap = sendBitmap;
                   ImgRequest.connectServer(requestBitmap); //비트맵 이미지를 전송하기위한 메소드 호출
                   try {
                       Thread.sleep(3500);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   Intent intent = new Intent(MainActivity.this, get_Img_Data.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                   startActivity(intent);
               }else if(sendBitmap==null) {
                   Toast.makeText(getApplicationContext(), "이미지를 선택 또는 촬영해 주세요.", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }//onCreate()

    //사진 촬영 또는 이미지 선택하여 이미지 비트맵 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //카메라로 사진 촬영 시
        if(requestCode == REQUEST_TAKE_PHOTO){
            if(resultCode==RESULT_OK){
                File file = new File(currentPhotoPath);
                ExifInterface exif = null;
                try{
                    exif = new ExifInterface(currentPhotoPath);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                    if (Build.VERSION.SDK_INT >= 29)
                    {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                        try
                        {
                            sendBitmap = ImageDecoder.decodeBitmap(source);
                            if (sendBitmap != null)
                            {

                                sendBitmap = rotateBitmap(sendBitmap,orientation);
                                imgV.setImageBitmap(sendBitmap);
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            sendBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                            if (sendBitmap != null)
                            {
                                sendBitmap = rotateBitmap(sendBitmap,orientation);
                                imgV.setImageBitmap(sendBitmap);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

            }
        }
        //앨범 에서 선택 시
        else if(requestCode == PICK_FROM_ALBUM){
            if(resultCode==RESULT_OK){
                imgUri = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(imgUri,filePath,null,null,null );
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                ExifInterface exif = null;
                try{
                    exif = new ExifInterface(imagePath);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                try {
                    sendBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                    sendBitmap = rotateBitmap(sendBitmap,orientation);
                    imgV.setImageBitmap(sendBitmap);
                } catch(IOException e) {
                }
            }
        }
    }//onActivityResult()

    private File createImageFile() throws IOException {
        // 사진파일을 만들어 임시저장소에 저장한다.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        //사진 촬영부분
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {

                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.plzfind.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution)      
    { //비트맵 리사이징 ** 긴 부분을 중심으로 비율을 맞춰 리사이징
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height)
        {
            if(maxResolution < width)
            {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        }
        else
        {
            if(maxResolution < height)
            {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        // 비트맵 회전 ** 이미지파일의 정보를 가져온 후 회전된 값을 기준으로 원래 방향으로 돌려놓는다.
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

}//MainActivity class

