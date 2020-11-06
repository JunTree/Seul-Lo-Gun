package com.example.sns_project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns_project.FirebaseHelper;
import com.example.sns_project.Tradeinfo;
import com.example.sns_project.R;
import com.example.sns_project.activity.CommentActivity;
import com.example.sns_project.activity.CommentTradeActivity;
import com.example.sns_project.activity.TradeActivity;
import com.example.sns_project.activity.WritePostActivity;
import com.example.sns_project.activity.WriteTradePostActivity;
import com.example.sns_project.listener.OnTradeListener;
import com.example.sns_project.view.ReadContentsView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.MainViewHolder> {
    private static final String TAG = "TradeAdapter";
    private ArrayList<Tradeinfo> mDataset;
    private Activity activity;
    private Tradeinfo tradeinfo;
    private FirebaseHelper firebaseHelper;
    private ArrayList<ArrayList<SimpleExoPlayer>> playerArrayListArrayList = new ArrayList<>();
    private final int MORE_INDEX = 2;
    private FirebaseUser user;
    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public TradeAdapter(Activity activity, ArrayList<Tradeinfo> mDataset) {
        this.mDataset = mDataset;
        this.activity = activity;

        firebaseHelper = new FirebaseHelper(activity);
    }

    public void setOnTradeListener(OnTradeListener onTradeListener){
        firebaseHelper.setOnTradeListener(onTradeListener);
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Nonnull
    @Override
    public TradeAdapter.MainViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trade_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, TradeActivity.class);
                intent.putExtra("tradeinfo", mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });

        cardView.findViewById(R.id.TradeMenu).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopup(v, mainViewHolder.getAdapterPosition());
        }
    });
        cardView.findViewById(R.id.comment_menu2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "댓글클릭 현재 tradeid :"  + tradeinfo.getId());
                Intent intent = new Intent(activity, CommentTradeActivity.class);
                intent.putExtra("tradeinfo",  mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });

        return mainViewHolder;
}

    @Override
    public void onBindViewHolder(@Nonnull final MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        user = FirebaseAuth.getInstance().getCurrentUser();
        TextView TradeTextView = cardView.findViewById((R.id.TradeTextView));

        tradeinfo = mDataset.get(position);
        TradeTextView.setText(tradeinfo.getTitle());

        ReadContentsView readContentsView = cardView.findViewById(R.id.readContentsView);
        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);

        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(tradeinfo)) {
            contentsLayout.setTag((tradeinfo));
            contentsLayout.removeAllViews();

                readContentsView.setMoreIndex(MORE_INDEX);
                readContentsView.setTradeinfo(tradeinfo);

                ArrayList<SimpleExoPlayer> playerArrayList = readContentsView.getTradePlayerArrayList();
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
                            myStartActivity(WriteTradePostActivity.class, mDataset.get(position));
                            return true;
                        case R.id.delete:

                            firebaseHelper.storageDelete2(mDataset.get(position));
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


    private void myStartActivity(Class c, Tradeinfo tradeinfo) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("tradeinfo", tradeinfo);
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