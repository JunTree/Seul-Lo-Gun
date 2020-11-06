package com.example.sns_project;


public class Userinfo {

    private String name;
    private String phoneNumber;
    private String birthDay;
    private String address;
    private String photoUrl;
    private String id;

    public Userinfo(String name, String phoneNumber, String birthDay, String address, String photoUrl,String id){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.photoUrl = photoUrl;
        this.id = id;
    }

    public Userinfo(String name, String phoneNumber, String birthDay, String address){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getBirthDay(){
        return this.birthDay;
    }
    public void setBirthDay(String birthDay){
        this.birthDay = birthDay;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getphotoUrl(){
        return this.photoUrl;
    }
    public void setphotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }


    public String getId(){
        return this.id;
    }
    public void setId(String id){ this.id = id;}

}