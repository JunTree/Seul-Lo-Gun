package com.example.sns_project.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sns_project.Comentinfo;
import com.example.sns_project.Postinfo;
import com.example.sns_project.R;
import com.example.sns_project.Storeinfo;
import com.example.sns_project.Tradeinfo;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class ReadContentsView extends LinearLayout{
    private static final String TAG = "ReadContentsView";
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList <SimpleExoPlayer> PostplayerArrayList = new ArrayList<>();
    private ArrayList <SimpleExoPlayer> TradeplayerArrayList = new ArrayList<>();
    private int moreIndex = -1;



    public ReadContentsView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ReadContentsView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initView();
    }

        private void initView(){
        setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_post,this,true);
    }

    public void setMoreIndex(int moreIndex) {
        this.moreIndex = moreIndex;
    }


    public void setPostinfo(Postinfo postinfo){
        TextView createdAtTextView = findViewById(R.id.createAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(postinfo.getCreatedAt()));


        LinearLayout contentsLayout = findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        ArrayList<String> contentsList = postinfo.getContents();
        ArrayList<String> formatList = postinfo.getFormats();

        for (int i = 0; i < contentsList.size(); i++) {
            if(i == moreIndex ){
                TextView textView = new TextView(context);
                textView.setLayoutParams(layoutParams);
                textView.setText("더보기..");
                contentsLayout.addView(textView);
                break;

            }
            String contents = contentsList.get(i);
            String formats = formatList.get(i);

            if(formats.equals("image")){
                ImageView imageView = (ImageView)layoutInflater.inflate(R.layout.view_contents_image, this, false);
                contentsLayout.addView(imageView);
                Glide.with(this).load(contents).override(1000).thumbnail(0.1f).into(imageView);
            }else if(formats.equals("video")){
                final PlayerView playerView =(PlayerView) layoutInflater.inflate(R.layout.view_contents_player, this, false);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, getResources().getString(R.string.app_name)));
                MediaSource videoSource =
                        new ProgressiveMediaSource.Factory(dataSourceFactory)
                                .createMediaSource(Uri.parse(contents));
                SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);
                player.prepare(videoSource);

                player.addVideoListener(new VideoListener() {
                    @Override
                    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                        playerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

                    }
                });

                PostplayerArrayList.add(player);
                playerView.setPlayer(player);
                contentsLayout.addView(playerView);
            }else{
                TextView textView = (TextView) layoutInflater.inflate(R.layout.view_contents_text, this, false);

                textView.setText(contents);

                contentsLayout.addView(textView);
            }
        }
    }


    public void setTradeinfo(Tradeinfo tradeinfo) {
        TextView createdAtTextView = findViewById(R.id.createAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tradeinfo.getCreatedAt()));

        LinearLayout contentsLayout = findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> contentsList = tradeinfo.getContents();
        ArrayList<String> formatList = tradeinfo.getFormats();

        for (int i = 0; i < contentsList.size(); i++) {
            if(i == moreIndex ){
                TextView textView = new TextView(context);
                textView.setLayoutParams(layoutParams);
                textView.setText("더보기..");
                contentsLayout.addView(textView);
                break;

            }

            String contents = contentsList.get(i);
            String formats = formatList.get(i);

            if(formats.equals("image")){
                ImageView imageView = (ImageView)layoutInflater.inflate(R.layout.view_contents_image, this, false);
                contentsLayout.addView(imageView);
                Glide.with(this).load(contents).override(1000).thumbnail(0.1f).into(imageView);
            }else if(formats.equals("video")){
                final PlayerView playerView =(PlayerView) layoutInflater.inflate(R.layout.view_contents_player, this, false);

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, getResources().getString(R.string.app_name)));
                MediaSource videoSource =
                        new ProgressiveMediaSource.Factory(dataSourceFactory)
                                .createMediaSource(Uri.parse(contents));
                SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);

                player.prepare(videoSource);

                player.addVideoListener(new VideoListener() {
                    @Override
                    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                        playerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                    }
                });

                TradeplayerArrayList.add(player);
                playerView.setPlayer(player);
                contentsLayout.addView(playerView);
            }else{
                TextView textView = (TextView) layoutInflater.inflate(R.layout.view_contents_text, this, false);
                textView.setText(contents);
                contentsLayout.addView(textView);
            }
        }
    }






    public ArrayList<SimpleExoPlayer> getPostPlayerArrayList() {
        return PostplayerArrayList;
    }
    public ArrayList<SimpleExoPlayer> getTradePlayerArrayList() {
        return TradeplayerArrayList;
    }
}

