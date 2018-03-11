package com.example.admin.litebulb.Models;

public class album{
    private String name;
    private int price;
    private String thumbnail;
    public album(){

    }
    public album(String name, int price, String thumbnail) {
        this.name = name;
        this.price = price;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
