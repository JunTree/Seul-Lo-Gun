package com.example.sns_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns_project.R;
import com.example.sns_project.Reviewinfo;
import com.example.sns_project.Storeinfo;
import com.example.sns_project.adapter.ReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.Date;

public class ReviewActivity extends BasicActivity {
    private static final String TAG = "ReviewActivity";
    private Reviewinfo reviewinfo;
    private Storeinfo storeinfo;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Reviewinfo> reviewList;
    private EditText reviewEditText;
    private TextView reviewText;
    private ImageView review;
    private RatingBar ratingBar;


    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Toast.makeText(ReviewActivity.this,"별점과 리뷰를 입력해주세요", Toast.LENGTH_LONG).show();
        storeinfo = (Storeinfo) getIntent().getSerializableExtra("storeinfo");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(storeinfo.getStoreName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.review_recycler_view);
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList);
        recyclerView.setAdapter(reviewAdapter);

        review = findViewById(R.id.review);
        reviewText = findViewById(R.id.reviewText);
        reviewEditText = findViewById(R.id.reviewEditText);
        ratingBar = findViewById(R.id.ratingBar2);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ratingBar.setIsIndicator(false);


            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {

                        String ratingvalue = (String.valueOf(rating));

                        review.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String comments = ((EditText) findViewById(R.id.reviewEditText)).getText().toString();
                                final Date date = reviewinfo == null ? new Date() : reviewinfo.getCreatedAt();
                                if (reviewEditText.getText().toString().equals("")) {
                                    Toast.makeText(ReviewActivity.this, "리뷰를 입력해주세요", Toast.LENGTH_SHORT).show();
                                } else {
                                    final DocumentReference docRef = firebaseFirestore.collection("stores").document(storeinfo.getStoreName());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                    String name = document.getData().get("storeName").toString();
                                                    Reviewinfo reviewinfo = new Reviewinfo(name, comments, user.getUid(), ratingvalue, date);
                                                    storeUpload(reviewinfo);
                                                    reviewEditText.setText("");
                                                    reviewList.clear();
                                                    readComments();
                                                    ratingBar.setRating(0);
                                                    Toast.makeText(ReviewActivity.this, "리뷰가 등록되었습니다", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "여기냐3 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
                                                } else {
                                                    Log.d(TAG, "No such document");

                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });



        Log.d(TAG, "리드커맨드 들어간다 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        readComments();


}



    private void storeUpload(Reviewinfo reviewinfo) {
        firebaseFirestore.collection("reviews")
                .add(reviewinfo.getReviewinfo())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        }


    private void readComments() {
        CollectionReference reivewsRef = firebaseFirestore.collection("reviews");
        reivewsRef.whereEqualTo("storeName", storeinfo.getStoreName()).orderBy("createdAt", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (storeinfo.getStoreName() != null) {
                    if (task.isSuccessful()) {
                        reviewList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            reviewList.add(new Reviewinfo(
                                    document.getData().get("storeName").toString(),
                                    document.getData().get("comment").toString(),
                                    document.getData().get("publisher").toString(),
                                    document.getData().get("rating").toString(),
                                    new Date(document.getDate("createdAt").getTime()),
                                    document.getId()));
                            Log.d(TAG, "가져와쓰");
                        }
                        reviewAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                        Log.d(TAG, "못가져와쓰");
                    }
                }
                else{
                    Log.d(TAG, "storeinfo.getStoreName()가 널이래 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ: ");

                }
            }
        });
    }



    private void myStartActivity(Class c, int requestCode) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, requestCode);
    }
}

