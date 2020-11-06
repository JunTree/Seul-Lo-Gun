package com.example.sns_project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sns_project.Comentinfo;
import com.example.sns_project.R;
import com.example.sns_project.Storeinfo;
import com.example.sns_project.Tradeinfo;
import com.example.sns_project.activity.CallActivity;
import com.example.sns_project.activity.PostActivity;
import com.example.sns_project.activity.ReviewActivity;
import com.example.sns_project.activity.StoreActivity;
import com.example.sns_project.activity.TradeActivity;
import com.example.sns_project.activity.WriteTradePostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MainViewHolder> {
    private static final String TAG = "StoreAdapter";
    private ArrayList<Storeinfo> mData;
    private Activity activity;
    private Storeinfo storeinfo;
    private ImageView contentsImageView;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);


    public StoreAdapter(ArrayList<Storeinfo> mData,Activity activity) {
        this.mData = mData;
        this.activity = activity;
    }


    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView contentsImageView;

        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
            contentsImageView = v.findViewById(R.id.contentsImageView);

        }


    }


    @NonNull
    @Override
    public StoreAdapter.MainViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foodstore, parent, false);
        StoreAdapter.MainViewHolder mainViewHolder = new StoreAdapter.MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       cardView.findViewById(R.id.cardimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, StoreActivity.class);
                intent.putExtra("storeinfo", mData.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);


            }
        });
        cardView.findViewById(R.id.review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReviewActivity.class);
                intent.putExtra("storeinfo", mData.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);

            }
        });
        cardView.findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "전화번호 :"  + storeinfo.getStoretel());
                Log.d(TAG, "전화번호 :"  + mData.get(mainViewHolder.getAdapterPosition()).getStoretel());
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mData.get(mainViewHolder.getAdapterPosition()).getStoretel()));
                activity.startActivity(intent);

            }
        });
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView storeimage = cardView.findViewById(R.id.storeimage);
        TextView storeName = cardView.findViewById(R.id.storeName);
        TextView Tag = cardView.findViewById(R.id.tag);

        storeinfo = mData.get(position);

        storeName.setText(storeinfo.getStoreName());
        Tag.setText(storeinfo.getTag());
        Glide.with(activity).load(mData.get(position).getPhotoUrl()).into(storeimage);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    private void myStartActivity(Class c, Storeinfo storeinfo) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("storeinfo", storeinfo);
        activity.startActivity(intent);
    }


}