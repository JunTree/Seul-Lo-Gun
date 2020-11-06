package com.example.sns_project;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Storeinfo implements Serializable  {
    private Drawable storeimage;
    private String storeName;
    private String Tag;
    private String storetel;
    private String photoUrl;
    private String id;

    public Storeinfo(String storeName, String Tag, String storetel, String photoUrl, String id){

        this.storeName = storeName;
        this.Tag = Tag;
        this.storetel = storetel;
        this.photoUrl = photoUrl;
        this.id = id;

    }


    public Storeinfo(String storeName, String Tag, String storetel, String photoUrl){

        this.storeName = storeName;
        this.Tag = Tag;
        this.storetel = storetel;
        this.photoUrl = photoUrl;

    }
    public Map<String, Object> getStoreinfo(){
        Map<String, Object> docData = new HashMap<>();

        docData.put("storeName",storeName);
        docData.put("Tag",Tag);
        docData.put("storetel",storetel);
        docData.put("photoUrl",photoUrl);
        return docData;




    }

    public String getStoreName(){ return this.storeName; }
    public void setStoreName(String storeName){ this.storeName = storeName; }
    public String getTag(){ return this.Tag; }
    public void setTag(String Tag){ this.Tag = Tag;}
    public String getStoretel() { return this.storetel; }
    public void setStoretel(String storetel) { this.storetel = storetel; }
    public String getPhotoUrl() { return this.photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public Drawable getStoreimage() { return this.storeimage; }
    public void setStoreimage(Drawable getStoreimage) { this.storeimage = storeimage; }
    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
}