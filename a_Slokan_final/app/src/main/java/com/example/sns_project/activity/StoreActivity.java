package com.example.sns_project.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sns_project.Storeinfo;
import com.example.sns_project.R;
public class StoreActivity extends BasicActivity {
    private ImageView contentsImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);

        Storeinfo storeinfo = (Storeinfo) getIntent().getSerializableExtra("storeinfo");
        contentsImageView = findViewById(R.id.contentsImageView);
        Glide.with(this).load(storeinfo.getPhotoUrl()).into(contentsImageView);
    }

}

