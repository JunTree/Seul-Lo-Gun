package com.example.sns_project.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sns_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";



    public HomeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_home, container, false);
            final Drawable starbucks = getResources().getDrawable(R.drawable.logo);
            final ImageView homeimageView = view.findViewById(R.id.homeimageView);
            final TextView homeTextView = view.findViewById(R.id.homeTextView);
            final TextView homeTextView2 = view.findViewById(R.id.homeTextView2);


            homeimageView.setImageDrawable(starbucks);
            homeTextView.setText("Director");
            homeTextView2.setText("이영한 최준근");

        return view;
    }

    @Override
    public  void onAttach(Context context){super.onAttach(context);}

    @Override
    public  void onDetach(){super.onDetach();}

    @Override
    public  void onPause(){super.onPause();}





}