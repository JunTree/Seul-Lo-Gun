package com.example.sns_project.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sns_project.Comentinfo;
import com.example.sns_project.FirebaseHelper;
import com.example.sns_project.R;
import com.example.sns_project.Reviewinfo;
import com.example.sns_project.Userinfo;
import com.example.sns_project.activity.ReviewActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG = "ReviewAdapter";
    private ArrayList<Reviewinfo> mReview;
    private Activity activity;
    private Reviewinfo reviewinfo;

    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public ReviewAdapter(Activity activity, ArrayList<Reviewinfo> mReview) {
        this.mReview = mReview;
        this.activity = activity;


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);


        }
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review, viewGroup, false);


        final ViewHolder mainViewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i ) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        View view = viewHolder.itemView;

        ImageView reviewDelete= view.findViewById(R.id.reviewDelete);
        TextView reviewText = view.findViewById(R.id.reviewText);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar2);
        TextView reviewtime = view.findViewById(R.id.reviewtime);
        reviewinfo = mReview.get(i);
        String A = mReview.get(i).getPublisher();
        String B = user.getUid();
        float rating = Float.parseFloat(mReview.get(i).getRating());

        reviewtime.setText(new SimpleDateFormat("yyyy-MM-dd"+" "+ "HH:mm", Locale.getDefault()).format(mReview.get(i).getCreatedAt()));
        ratingBar.setRating(rating);
        reviewText.setText(mReview.get(i).getComment());

        reviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(TAG, "바깥"+"publisher 값:" + A +"user.getUid 값: "+ B);
                if(A.equals(B)) {

                    firebaseFirestore.collection("reviews").document(mReview.get(i).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    Log.w(TAG, "성공"+"publisher 값 :" +A +"user.getUid 값:"+ user.getUid());

                                    mReview.remove(i);
                                    notifyItemRemoved(i);
                                    notifyItemRangeChanged(i, getItemCount());
                                    notifyDataSetChanged();
                                    Toast.makeText(activity, "리뷰를 지웠습니다.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                }
                else{

                    Log.w(TAG, "실패"+"publisher 값 :" + A +"  user.getUid 값 : "+ B);
                }
            }
        });

    }


    @Override
    public int getItemCount() {


        return mReview.size();
    }


}
