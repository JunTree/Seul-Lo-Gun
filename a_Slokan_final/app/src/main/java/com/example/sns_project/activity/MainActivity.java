package com.example.sns_project.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.sns_project.R;
import com.example.sns_project.fragment.CommunicationFragment;
import com.example.sns_project.fragment.FoodStoreFragment;
import com.example.sns_project.fragment.HomeFragment;
import com.example.sns_project.fragment.TradeFragment;
import com.example.sns_project.fragment.UesrinfoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends BasicActivity {
        private static final String TAG = "MainActivity";
        private FirebaseUser firebaseUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (firebaseUser == null) {
                        myStartActivity(SignUpActivity.class);
                } else {
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null) {
                                                        if (document.exists()) {
                                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                        } else {
                                                                Log.d(TAG, "No such document");
                                                                myStartActivity(UserinitActivity.class);
                                                        }
                                                }
                                        } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                        }
                                }
                        });

                        HomeFragment homeFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, homeFragment)
                                .commit();
                        setToolbarTitle(getResources().getString(R.string.home));

                        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigationView);
                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
                                @Override
                                public boolean onNavigationItemSelected(@NonNull MenuItem item){
                                        switch(item.getItemId()){
                                                case R.id.home:
                                                        Log.e("홈","홈");
                                                        HomeFragment homeFragment = new HomeFragment();
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.container, homeFragment)
                                                                .commit();
                                                        setToolbarTitle(getResources().getString(R.string.home));
                                                        return true;
                                                case R.id.food:
                                                        Log.e("음식점","음식점");
                                                        FoodStoreFragment foodStoreFragment = new FoodStoreFragment();
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.container, foodStoreFragment)
                                                                .commit();
                                                        setToolbarTitle(getResources().getString(R.string.food));
                                                        return true;

                                                case R.id.communication:
                                                        Log.e("자유게시판","자유게시판");
                                                        CommunicationFragment communicationFragment = new CommunicationFragment();
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.container, communicationFragment)
                                                                .commit();
                                                        setToolbarTitle(getResources().getString(R.string.com));
                                                        return true;

                                                case R.id.Trade:
                                                        Log.e("중고거래","중고거래");
                                                        TradeFragment tradeFragment = new TradeFragment();
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.container, tradeFragment)
                                                                .commit();
                                                        setToolbarTitle(getResources().getString(R.string.trade));
                                                        return true;

                                                case R.id.userinfo:
                                                        Log.e("내정보","내정보");
                                                        UesrinfoFragment uesrinfoFragment = new UesrinfoFragment();
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.container, uesrinfoFragment)
                                                                .commit();
                                                        setToolbarTitle(getResources().getString(R.string.uesrinfo));
                                                        return true;


                                        }
                                        return false;
                                }
                        });

                }
        }

        @Override
        protected void onResume() {
                super.onResume();
        }

        @Override
        protected  void onPause(){
                super.onPause();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                switch (requestCode) {
                        case 0:
                                if(data != null){
                                       // postUpdate(false);
                                }
                                break;
                }
        }
        private void myStartActivity(Class c) {
                Intent intent = new Intent(this, c);
                startActivityForResult(intent, 0);
        }

}