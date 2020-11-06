package com.example.sns_project.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.sns_project.FirebaseHelper;
import com.example.sns_project.R;
import com.example.sns_project.Tradeinfo;
import com.example.sns_project.listener.OnTradeListener;
import com.example.sns_project.view.ReadContentsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TradeActivity extends BasicActivity {
    private Tradeinfo tradeinfo;
    private FirebaseHelper firebaseHelper;
    private ReadContentsView readContentsView;
    private LinearLayout contentsLayout;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        user = FirebaseAuth.getInstance().getCurrentUser();
        tradeinfo = (Tradeinfo) getIntent().getSerializableExtra("tradeinfo");

        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsView = findViewById(R.id.readContentsView);

        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnTradeListener(onTradeListener);
        uiUpdate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    tradeinfo = (Tradeinfo) data.getSerializableExtra("tradeinfo");
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
        String A = tradeinfo.getPublisher();
        String B = user.getUid();
        if(A.equals(B)){
            switch (item.getItemId()) {
                case R.id.delete:
                    firebaseHelper.storageDelete2(tradeinfo);
                    finish();
                    return true;
                case R.id.modify:
                    myStartActivity(WriteTradePostActivity.class, tradeinfo);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    OnTradeListener onTradeListener = new OnTradeListener() {

        @Override
        public void onDelete2(Tradeinfo tradeinfo) {
            Log.e("로그 ", "삭제 성공");
        }

        @Override
        public void onModify2() {
            Log.e("로그 ", "수정 성공");
        }
    };

    private void uiUpdate(){
        setToolbarTitle(tradeinfo.getTitle());
        readContentsView.setTradeinfo(tradeinfo);
    }

    private void myStartActivity(Class c, Tradeinfo tradeinfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("tradeinfo", tradeinfo);
        startActivityForResult(intent, 0);
    }
}

