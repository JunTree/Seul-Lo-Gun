package com.example.sns_project;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comentinfo implements Serializable {

    private String name;
    private String comment;
    private String postid;
    private String publisher;
    private String photoUrl;
    private Date createdAt;
    private String id;

    public Comentinfo( String name, String comment, String publisher, String postid,String photoUrl, Date createdAt,String id){
        this.name = name;
        this.comment = comment;
        this.postid = postid;
        this.publisher = publisher;
        this.photoUrl = photoUrl;
        this.createdAt = createdAt;
        this.id = id;
    }


    public Comentinfo(String name, String comment, String publisher, String postid ,String photoUrl,Date createdAt) {
        this.name = name;
        this.comment = comment;
        this.postid = postid;
        this.publisher = publisher;
        this.photoUrl = photoUrl;
        this.createdAt = createdAt;

    }

    public Map<String, Object> getComentinfo(){
        Map<String, Object> docData = new HashMap<>()   ;

        docData.put("name",name);
        docData.put("comment",comment);
        docData.put("publisher", publisher);
        docData.put("postid", postid);
        docData.put("photoUrl", photoUrl);
        docData.put("createdAt", createdAt);


        return docData;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){ this.name = name; }

    public String getPublisher(){ return this.publisher; }
    public void setPublisher(String publisher){ this.publisher = publisher; }

    public String getComment(){
        return this.comment;
    }
    public void setComment(String comment){ this.comment = comment; }

    public String getPostid(){
        return this.postid;
    }
    public void setPostid(String postid){ this.postid = postid; }


    public String getPhotoUrl(){
        return this.photoUrl;
    }
    public void setPhotoUrl(String photoUrl){ this.photoUrl = photoUrl; }


    public Date getCreatedAt(){
        return this.createdAt;
    }
    public void setCreatedAt(Date createdAt){ this.createdAt = createdAt; }

    public String getId(){
        return this.id;
    }
    public void setId(String id){ this.id = id;}

}