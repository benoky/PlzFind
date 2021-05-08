package com.example.plzfind;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class get_Img_Data extends MainActivity{

    Button go_connect_market;
    ImageView learning_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_imgdata);



        Intent intent = getIntent();

        String getproname = intent.getExtras().getString("pro_name");
        byte[] arr = getIntent().getByteArrayExtra("pro_image");
        Bitmap getproimg = BitmapFactory.decodeByteArray(arr,0,arr.length);

        learning_img = (ImageView)findViewById(R.id.GetData_Img);
        learning_img.setImageBitmap(getproimg);


        go_connect_market = (Button)findViewById(R.id.Go_Connect_Market);
        go_connect_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(get_Img_Data.this, Connect_Market.class);
                i.putExtra("pro_name", getproname);
                startActivity(i);
            }
        });
    }

}
