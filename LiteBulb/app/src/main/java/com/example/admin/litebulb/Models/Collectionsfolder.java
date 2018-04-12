package com.example.admin.litebulb.Models;

public class Collectionsfolder{
    private String votes, items, name, user_name;
    private int user_id, id;
    private String thumbnail;
    public Collectionsfolder(){

    }
    public Collectionsfolder(int user_id, int id, String name, String votes, String items, String user_name, String thumbnail) {
        this.name = name;
        this.user_id=user_id;
        this.id=id;
        this.votes = votes;
        this.items=items;
        this.user_name=user_name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getVotes() {
        return votes;
    }
    public void setVotes(String votes) {
        this.votes = votes;
    }


    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getItems() {
        return items;
    }
    public void setItems(String items) {
        this.items = items;
    }

    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


}
