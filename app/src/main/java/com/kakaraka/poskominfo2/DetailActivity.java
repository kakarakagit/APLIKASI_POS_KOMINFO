package com.kakaraka.poskominfo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.kakaraka.poskominfo2.MainActivity.EXTRA_NAMA;
import static com.kakaraka.poskominfo2.MainActivity.EXTRA_NOIZIN;
import static com.kakaraka.poskominfo2.MainActivity.EXTRA_KODEPOS;
import static com.kakaraka.poskominfo2.MainActivity.EXTRA_MASABERLAKU;

public class DetailActivity extends AppCompatActivity {

    private TextView namaDetail, noizinDetail, kodeposDetail, masaberlakuDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String namanama = intent.getStringExtra(EXTRA_NAMA);
        String noizinnoizin = intent.getStringExtra((EXTRA_NOIZIN));
        String kodeposkodepos = intent.getStringExtra(EXTRA_KODEPOS);
        String masabermasaber = intent.getStringExtra(EXTRA_MASABERLAKU);


        namaDetail = findViewById(R.id.name_detail);
        noizinDetail = findViewById(R.id.phone_detail);
        kodeposDetail = findViewById(R.id.status_detail);
        masaberlakuDetail = findViewById(R.id.link_gmaps_detail);


        namaDetail.setText(namanama);
        noizinDetail.setText(noizinnoizin);
        kodeposDetail.setText(kodeposkodepos);
        masaberlakuDetail.setText(masabermasaber);

    }
}
