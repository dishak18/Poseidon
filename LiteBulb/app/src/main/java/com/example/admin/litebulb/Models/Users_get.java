package com.example.admin.litebulb.Models;

public class Users_get{
    private String username;
    private String featured_author, thumbnail;
    private int items, sales;
    public Users_get(String username, String title, String shortdesc, double rating, double price, String image){

    }
    public Users_get()
    {

    }
    public Users_get(String username, String featured_author, int sales, int items, String thumbnail) {
        this.username = username;
        this.featured_author = featured_author;
        this.items=items;
        this.thumbnail=thumbnail;
        this.sales=sales;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFeatured_author() {
        return featured_author;
    }

    public void setFeatured_author(String featured_author) {
        this.featured_author = featured_author;
    }
    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }



}