package com.example.sns_project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns_project.Comentinfo;
import com.example.sns_project.FirebaseHelper;
import com.example.sns_project.Postinfo;
import com.example.sns_project.R;
import com.example.sns_project.activity.CommentActivity;
import com.example.sns_project.activity.PostActivity;
import com.example.sns_project.activity.WritePostActivity;
import com.example.sns_project.listener.OnPostListener;
import com.example.sns_project.view.ReadContentsView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import javax.annotation.Nonnull;


public class CommunicationAdapter extends RecyclerView.Adapter<CommunicationAdapter.MainViewHolder> {
    private static final String TAG = "CommunicationAdapter";
    private ArrayList<Postinfo> mDataset;
    private Postinfo postinfo;
    private Comentinfo comentinfo;
    private Activity activity;
    private ImageView comments;
    private FirebaseHelper firebaseHelper;
    private ArrayList<ArrayList<SimpleExoPlayer>> playerArrayListArrayList = new ArrayList<>();
    private final int MORE_INDEX = 2;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    static class MainViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;

            MainViewHolder(@NonNull CardView v) {
                super(v);
                cardView = v;
            }

    }

    public CommunicationAdapter(Activity activity, ArrayList<Postinfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;

        firebaseHelper = new FirebaseHelper(activity);
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Nonnull
    @Override
    public CommunicationAdapter.MainViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, PostActivity.class);
                intent.putExtra("postinfo", mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });

        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, mainViewHolder.getAdapterPosition());
            }
        });

        cardView.findViewById(R.id.comment_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "댓글클릭 현재 postid :"  + postinfo.getId());
                Intent intent = new Intent(activity, CommentActivity.class);
                intent.putExtra("postinfo",  mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });


        return mainViewHolder;
    }


    @Override
    public void onBindViewHolder(@Nonnull final MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById((R.id.titleTextView));
        user = FirebaseAuth.getInstance().getCurrentUser();
        postinfo = mDataset.get(position);

        titleTextView.setText(postinfo.getTitle());

        ReadContentsView readContentsView = cardView.findViewById(R.id.readContentsView);
        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);

        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(postinfo)) {
            contentsLayout.setTag((postinfo));
            contentsLayout.removeAllViews();

            readContentsView.setMoreIndex(MORE_INDEX);
            readContentsView.setPostinfo(postinfo);

            ArrayList<SimpleExoPlayer> playerArrayList = readContentsView.getPostPlayerArrayList();
            if(playerArrayList != null){
                playerArrayListArrayList.add(playerArrayList);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    private void showPopup(View v, int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        String A = mDataset.get(position).getPublisher();
        String B = user.getUid();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(A.equals(B)){
                    switch (menuItem.getItemId()) {
                        case R.id.modify:
                            myStartActivity(WritePostActivity.class, mDataset.get(position));
                            return true;
                        case R.id.delete:

                            firebaseHelper.storageDelete(mDataset.get(position));
                            return true;
                        default:
                            return false;
                    }
                }
                return false;
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }


    private void myStartActivity(Class c, Postinfo postinfo) {
            Intent intent = new Intent(activity, c);
            intent.putExtra("postinfo", postinfo);
            activity.startActivity(intent);
    }

    public void playerStop(){
        for (int i = 0; i < playerArrayListArrayList.size(); i++){
            ArrayList<SimpleExoPlayer> playerArrayList = playerArrayListArrayList.get(i);
            for (int ii = 0; ii < playerArrayList.size(); ii++){
                SimpleExoPlayer player = playerArrayList.get(ii);
                if(player.getPlayWhenReady()){
                    player.setPlayWhenReady(false);
                }
            }
        }
    }

}