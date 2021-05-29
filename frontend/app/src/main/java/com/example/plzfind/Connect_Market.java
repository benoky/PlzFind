package com.example.plzfind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Connect_Market extends MainActivity {

    Button interpark_btn;
    Button auction_btn;
    Button wemakeprice_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_market);

        Intent intent = getIntent();
        String getproname = intent.getExtras().getString("pro_name");   // 제품명 받는 부분
        TextView pro_market = (TextView)findViewById(R.id.promarket);
        pro_market.setText(getproname);

        interpark_btn = (Button)findViewById(R.id.Interpark_Btn);
        interpark_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.shop.interpark.com/search_all/?q="+getproname+"&type=all"));
                startActivity(i);
            }
        });

        auction_btn = (Button)findViewById(R.id.Auction_Btn);
        auction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://browse.auction.co.kr/m/search?keyword="+getproname));
                startActivity(i);
            }
        });

        wemakeprice_btn = (Button)findViewById(R.id.Wemakeprice_Btn);
        wemakeprice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msearch.wemakeprice.com/search?keyword="+getproname+"&_service=5"));
                startActivity(i);
            }
        });


    }


}
