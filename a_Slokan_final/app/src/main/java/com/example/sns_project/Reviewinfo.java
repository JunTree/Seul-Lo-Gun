package com.example.sns_project;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Reviewinfo implements Serializable {

    private String storeName;
    private String comment;
    private String publisher;
    private String rating;
    private Date createdAt;
    private String id;

    public Reviewinfo(String storeName, String comment, String publisher, String rating , Date createdAt , String id){
        this.storeName = storeName;
        this.comment = comment;
        this.publisher = publisher;
        this.rating = rating;
        this.createdAt = createdAt;
        this.id = id;
    }


    public Reviewinfo(String storeName, String comment, String publisher, String rating,  Date createdAt)  {
        this.storeName = storeName;
        this.comment = comment;
        this.publisher = publisher;
        this.rating = rating;
        this.createdAt = createdAt;

    }

    public Map<String, Object> getReviewinfo(){
        Map<String, Object> docData = new HashMap<>();

        docData.put("storeName",storeName);
        docData.put("comment",comment);
        docData.put("publisher", publisher);
        docData.put("rating", rating);
        docData.put("createdAt", createdAt);


        return docData;
    }

    public String getStoreName(){
        return this.storeName;
    }
    public void setStoreName(String name){ this.storeName = storeName; }

    public String getPublisher(){
        return this.publisher;
    }
    public void setPublisher(String publisher){ this.publisher = publisher; }

    public String getComment(){
        return this.comment;
    }
    public void setComment(String comment){ this.comment = comment; }

    public String getRating(){
        return this.rating;
    }
    public void setRating(String rating){ this.rating = rating; }


    public Date getCreatedAt(){
        return this.createdAt;
    }
    public void setCreatedAt(Date createdAt){ this.createdAt = createdAt; }

    public String getId(){
        return this.id;
    }
    public void setId(String id){ this.id = id;}

}