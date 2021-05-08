package com.example.plzfind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Connect_Market extends MainActivity {

    Button naver_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_market);


        String g302 = "G302";   // 제품명 받는 부분

        naver_btn = (Button)findViewById(R.id.Naver_Btn);
        naver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.shop.interpark.com/search_all/?q="+g302+"&type=all"));
                startActivity(i);
            }
        });
    }


}
