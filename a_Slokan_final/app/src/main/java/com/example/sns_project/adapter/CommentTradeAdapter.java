package com.example.sns_project.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sns_project.ComentTradeinfo;
import com.example.sns_project.Comentinfo;
import com.example.sns_project.FirebaseHelper;
import com.example.sns_project.R;
import com.example.sns_project.Userinfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CommentTradeAdapter extends RecyclerView.Adapter<CommentTradeAdapter.ViewHolder> {

    private static final String TAG = "CommentTradeAdapter";
    private ArrayList<ComentTradeinfo> mComment;
    private Activity activity;
    private Userinfo userinfo;
    private ComentTradeinfo comentTradeinfo;


    private FirebaseUser user;
    private FirebaseHelper firebaseHelper;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public CommentTradeAdapter(Activity activity, ArrayList<ComentTradeinfo> mComment) {
        this.mComment = mComment;
        this.activity = activity;


    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public CommentTradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);

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

        comentTradeinfo = mComment.get(i);
        String A = mComment.get(i).getPublisher();
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
                Log.w(TAG, "getId값: " + mComment.get(i).getId());
                if(A.equals(B)) {

                    firebaseFirestore.collection("commentTrade").document(comentTradeinfo.getId())
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
    public int getItemCount() {


        return mComment.size();
    }
}
