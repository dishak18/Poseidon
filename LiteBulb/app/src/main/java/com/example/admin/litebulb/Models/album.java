package com.example.admin.litebulb.Models;

public class album{
    private String name;
    private int price, id;
    private String thumbnail;
    public album(){

    }
    public album(int id, String name, int price, String thumbnail) {
        this.name = name;
        this.id=id;
        this.price = price;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getprice() {
        return price;
    }

    public void setprice(int price) {
        this.price = price;
    }
    

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


}
