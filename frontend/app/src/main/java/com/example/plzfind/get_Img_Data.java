package com.example.plzfind;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class get_Img_Data extends MainActivity{

    Button go_connect_market;
    ImageView learning_img;
    static String[] pro_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_imgdata);



        learning_img = (ImageView)findViewById(R.id.GetData_Img);
        Bitmap bitmap=ImgRequest.getBitmap();

        while(true){
            if(bitmap!=null){
                break;
            }
            bitmap=ImgRequest.getBitmap();
        }
        learning_img.setImageBitmap(bitmap); //서버에서 받은 이미지를 화면에 출력
        pro_name = ImgRequest.retrunStr;
        while(true){
            if(pro_name!=null){
                break;
            }
            pro_name = ImgRequest.retrunStr;
        }


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, pro_name);
        ListView pro_list = (ListView)findViewById(R.id.ProList);
        pro_list.setAdapter(adapter);

        pro_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strText = (String)parent.getItemAtPosition(position);


                Intent i = new Intent(get_Img_Data.this, Connect_Market.class);
                i.putExtra("pro_name", strText);

                startActivity(i);



            }
        });



    }
}
