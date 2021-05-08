package com.example.plzfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class get_Img_Data extends MainActivity{

    Button go_connect_market;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_imgdata);

        go_connect_market = (Button)findViewById(R.id.Go_Connect_Market);
        go_connect_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(get_Img_Data.this, Connect_Market.class);
                startActivity(i);
            }
        });
    }

}
