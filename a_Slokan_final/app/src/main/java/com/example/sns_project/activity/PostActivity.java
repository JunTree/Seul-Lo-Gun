package com.example.sns_project.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.widget.PopupMenu;

import com.example.sns_project.ComentTradeinfo;
import com.example.sns_project.FirebaseHelper;
import com.example.sns_project.Postinfo;
import com.example.sns_project.R;
import com.example.sns_project.adapter.CommentTradeAdapter;
import com.example.sns_project.adapter.CommunicationAdapter;
import com.example.sns_project.listener.OnPostListener;
import com.example.sns_project.view.ReadContentsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PostActivity extends BasicActivity {
    private static final String TAG = "PostActivity";
    private Postinfo postinfo;
    private FirebaseHelper firebaseHelper;
    private ReadContentsView readContentsView;
    private LinearLayout contentsLayout;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        user = FirebaseAuth.getInstance().getCurrentUser();

        postinfo = (Postinfo) getIntent().getSerializableExtra("postinfo");

        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsView = findViewById(R.id.readContentsView);

        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);
        uiUpdate();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    postinfo = (Postinfo)data.getSerializableExtra("postinfo");
                    contentsLayout.removeAllViews();
                    uiUpdate();
                }
                break;
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String A = postinfo.getPublisher();
        String B = user.getUid();
        if(A.equals(B)){
            switch (item.getItemId()) {
                case R.id.delete:
                    firebaseHelper.storageDelete(postinfo);
                    finish();
                    return true;
                case R.id.modify:
                    myStartActivity(WritePostActivity.class, postinfo);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(Postinfo postinfo) {
            Log.e("로그 ", "삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그 ", "수정 성공");
        }
    };


    private void uiUpdate(){
        setToolbarTitle(postinfo.getTitle());
        readContentsView.setPostinfo(postinfo);
    }

    private void myStartActivity(Class c, Postinfo postinfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("postinfo", postinfo);
        startActivityForResult(intent, 0);
    }
}

