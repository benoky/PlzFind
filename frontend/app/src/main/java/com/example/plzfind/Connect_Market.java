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

    Button gmarket_btn;
    Button tmon_btn;
    Button coupang_btn;

    Button naver_btn;
    Button eleven_btn;
    Button amazon_btn;

    Button go_first;

    String getproname = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_market);

        Intent intent = getIntent();
        String getproname = intent.getExtras().getString("pro_name");   // 제품명 받는 부분
        TextView pro_market = (TextView)findViewById(R.id.promarket);
        pro_market.setText("Market\n"+getproname);

        ClickMarket(getproname);

        go_first = (Button)findViewById(R.id.Go_first);
        go_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Connect_Market.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                recreate();
                startActivity(intent);
            }
        });
    }

    public void ClickMarket(String tem_proname){

        //1번째줄 버튼 ###############################################################################################################################################################
        interpark_btn = (Button)findViewById(R.id.Interpark_Btn);
        interpark_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://m.shop.interpark.com/search_all/?q="+tem_proname+"&type=all"));
                startActivity(i);
            }
        });

        auction_btn = (Button)findViewById(R.id.Auction_Btn);
        auction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://browse.auction.co.kr/m/search?keyword="+tem_proname));
                startActivity(i);
            }
        });

        wemakeprice_btn = (Button)findViewById(R.id.Wemakeprice_Btn);
        wemakeprice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "https://msearch.wemakeprice.com/search?keyword="+tem_proname+"&_service=5"));
                startActivity(i);
            }
        });


        //2번째줄 버튼 ###############################################################################################################################################################
        gmarket_btn = (Button)findViewById(R.id.Gmarket_Btn);
        gmarket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "https://browse.gmarket.co.kr/m/search?keyword="+tem_proname));
                startActivity(i);
            }
        });

        tmon_btn = (Button)findViewById(R.id.Tmon_Btn);
        tmon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://m.search.tmon.co.kr/search?useArtistchaiRegion=Y#_=1622271298553&keyword="+tem_proname+"&sortType=POPULAR&thr=ms&useTypoCorrection=true"));
                startActivity(i);
            }
        });

        coupang_btn = (Button)findViewById(R.id.Coupang_Btn);
        coupang_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "https://m.coupang.com/nm/search?q="+tem_proname));
                startActivity(i);
            }
        });


        //3번째줄 버튼 ###############################################################################################################################################################
        naver_btn = (Button)findViewById(R.id.Naver_Btn);
        naver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "https://msearch.shopping.naver.com/search/all?query="+tem_proname+"&frm=NVSHSRC&cat_id=&pb=true&mall="));


                startActivity(i);
            }
        });

        eleven_btn = (Button)findViewById(R.id.Eleven_Btn);
        eleven_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://search.11st.co.kr/MW/search?searchKeyword="+tem_proname+"&decSearchKeyword=G304#_filterKey=1622272864093"));
                startActivity(i);
            }
        });

        amazon_btn = (Button)findViewById(R.id.Amazon_Btn);
        amazon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "https://www.amazon.com/s?k="+tem_proname+"&ref=nb_sb_noss"));
                startActivity(i);
            }
        });
    }
}
