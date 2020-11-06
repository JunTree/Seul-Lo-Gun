package com.example.sns_project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sns_project.Comentinfo;
import com.example.sns_project.FirebaseHelper;
import com.example.sns_project.R;
import com.example.sns_project.Userinfo;
import com.example.sns_project.activity.CommentActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private static final String TAG = "CommentAdapter";
    private ArrayList<Comentinfo> mComment;
    private Activity activity;
    private Userinfo userinfo;
    private Comentinfo comentinfo;

    private FirebaseUser user;
    private FirebaseHelper firebaseHelper;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public CommentAdapter(Activity activity, ArrayList<Comentinfo> mComment) {
        this.mComment = mComment;
        this.activity = activity;


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);

        }

    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (View)LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);

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
        ImageView image_profile2 = view.findViewById(R.id.image_profile2);
        ImageView commentDelete= view.findViewById(R.id.commentDelete);
        TextView username = view.findViewById(R.id.username);
        TextView comment = view.findViewById(R.id.comment);
        TextView commenttime = view.findViewById(R.id.commenttime);

        comentinfo = mComment.get(i);
        String A =mComment.get(i).getPublisher();
        String B = user.getUid();
        username.setText(mComment.get(i).getName());
        comment.setText(mComment.get(i).getComment());
        commenttime.setText(new SimpleDateFormat("MM/dd"+" "+"HH:mm", Locale.getDefault()).format(mComment.get(i).getCreatedAt()));
        if(mComment.get(i).getPhotoUrl()!=null){

            Glide.with(activity).load(mComment.get(i).getPhotoUrl()).into(image_profile2);
        }
        if(mComment.get(i).getPhotoUrl()==null){

            image_profile2.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }

            commentDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w(TAG, "바깥"+"publisher 값:" + A +"user.getUid 값: "+ B);
                    if(A.equals(B)) {

                    firebaseFirestore.collection("comments").document(mComment.get(i).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    Log.w(TAG, "성공"+"publisher 값 :" +A +"user.getUid 값:"+ user.getUid());

                                    mComment.remove(i);
                                    notifyItemRemoved(i);
                                    notifyItemRangeChanged(i, getItemCount());
                                    notifyDataSetChanged();
                                    Toast.makeText(activity, "댓글을 지웠습니다.", Toast.LENGTH_SHORT).show();
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
    public int getItemCount() { return mComment.size(); }

    private void myStartActivity(Class c, Comentinfo comentinfo) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("comentinfo", comentinfo);
        activity.startActivity(intent);
    }







  /*  private void delete2() {
        DocumentReference docRef = firebaseFirestore.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        // String userid =  document.getData().get("id").toString();
                        // if(userid == mComment.get(i).getPublisher()){
                        delete();
                        mComment.remove(i);
                        notifyItemRemoved(i);
                        delete();
                        notifyItemRangeChanged(i, getItemCount());
                        // }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }


   */

}
