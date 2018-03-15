package com.example.admin.litebulb.Models;

public class ItemClick{
    private String name;
    private int price, prepaid_price;
    private String thumbnail, description;
    public ItemClick(){

    }
    public ItemClick(String name, int price, String thumbnail, int prepaid_price, String description) {
        this.name = name;
        this.price = price;
        this.thumbnail = thumbnail;
        this.prepaid_price=prepaid_price;
        this.description=description;
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

    public int getPrepaid_price() {
        return prepaid_price;
    }

    public void setPrepaid_price(int prepaid_price) {
        this.prepaid_price = prepaid_price;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


}
