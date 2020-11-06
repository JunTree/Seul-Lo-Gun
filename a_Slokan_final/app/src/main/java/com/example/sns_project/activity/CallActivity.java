package com.example.sns_project.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.sns_project.Storeinfo;

public class CallActivity extends BasicActivity {
    private static final String TAG = "CallActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Storeinfo storeinfo = (Storeinfo) getIntent().getSerializableExtra("storeinfo");

        Log.d(TAG, "전화번호 :"  + storeinfo.getStoretel());
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + storeinfo.getStoretel()));
        startActivity(intent);

    }

}

