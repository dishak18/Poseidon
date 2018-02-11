package com.example.admin.litebulb;

public class album extends ImageLoader{
    private String name;
    private String price;
    private String thumbnail;
    public album(){

    }


    public album(String name, String price, String thumbnail) {
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

    public String getprice() {
        return price;
    }

    public void setprice(String price) {
        this.price = price;
    }
    

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


}
