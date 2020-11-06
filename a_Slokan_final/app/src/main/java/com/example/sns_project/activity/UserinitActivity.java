package com.example.sns_project.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.sns_project.Userinfo;
import com.example.sns_project.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.sns_project.Util.INTENT_PATH;
import static com.example.sns_project.Util.showToast;

public class UserinitActivity extends BasicActivity {
    private  static final String TAG = "UserinitActivity";
    private ImageView profileImageView;
    private RelativeLayout loaderLayout;
    private RelativeLayout ButtonsBackgroundLayout;
    private String profilePath;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_init);
        setToolbarTitle("회원정보");


        loaderLayout = findViewById(R.id.loaderLayout);
        profileImageView = findViewById(R.id.profileimageView);
        ButtonsBackgroundLayout = findViewById(R.id.buttonsBackgroundLayout);

        ButtonsBackgroundLayout.setOnClickListener(onClickListener);
        profileImageView.setOnClickListener(onClickListener);

        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
        findViewById(R.id.picture).setOnClickListener(onClickListener);
        findViewById(R.id.gallery).setOnClickListener(onClickListener);




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myStartActivity(SignUpActivity.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0 : {
                if (resultCode == Activity.RESULT_OK) {
                    profilePath = data.getStringExtra(INTENT_PATH);
                    Glide.with(this) .load(profilePath).centerCrop().override(500).into(profileImageView);
                    ButtonsBackgroundLayout.setVisibility(View.GONE);
            }
                break;
                }
            }
        }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.checkButton:
                storageUploader();
                break;
            case R.id.profileimageView:
                ButtonsBackgroundLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonsBackgroundLayout:
                ButtonsBackgroundLayout.setVisibility(View.GONE);
                break;
            case R.id.picture:
                myStartActivity(CameraActivity.class);
                break;
            case R.id.gallery:
                myStartActivity(GalleryActivity.class);
                break;
        }
    };

    private void storageUploader() {
        final String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        final String phoneNumber = ((EditText) findViewById(R.id.phoneNumberEditText)).getText().toString();
        final String birthDay = ((EditText) findViewById(R.id.birthDayEditText)).getText().toString();
        final String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();
        if(name.length() > 0 && phoneNumber.length() > 9 && birthDay.length() > 5 && address.length() > 0){
            loaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference ImageRef = storageRef.child("users/"+user.getUid()+"/profileImage.jpg");

            if(profilePath == null){
                Userinfo userinfo = new Userinfo(name,phoneNumber, birthDay, address);
                storeUploader(userinfo);

            } else{
                try{
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = ImageRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return ImageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();


                                Userinfo userinfo = new Userinfo(name, phoneNumber, birthDay, address, downloadUri.toString(),user.getUid());
                                storeUploader(userinfo);
                            } else {
                                showToast(UserinitActivity.this,"회원정보를 보내는데 실패하였습니다.");
                            }
                        }
                    });
                }catch (FileNotFoundException e){
                    Log.e("로그", "에러: "+toString());
                }
            }

        }else {
            showToast(UserinitActivity.this,"회원정보를 입력해 주세요.");
        }
    }

    private  void storeUploader(Userinfo userinfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(userinfo)
                .addOnSuccessListener(aVoid -> {
                    showToast(UserinitActivity.this,"회원정보 등록을 성공하였습니다.");
                    loaderLayout.setVisibility(View.GONE);
                    finish();
                })
                .addOnFailureListener(e -> {
                    showToast(UserinitActivity.this,"회원정보 등록에 실패하였습니다.");
                    loaderLayout.setVisibility(View.GONE);
                    Log.w(TAG, "Error writing document", e);
                });
    }
    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }

}