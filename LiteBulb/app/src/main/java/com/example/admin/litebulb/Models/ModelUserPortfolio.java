package com.example.admin.litebulb.Models;

public class ModelUserPortfolio{
    private String categories, sales, item_name, user_name;
    private int item_id, price;
    private String thumbnail;
    public ModelUserPortfolio(){

    }
    public ModelUserPortfolio(int item_id, int price, String item_name, String categories, String sales, String user_name, String thumbnail) {
        this.item_name = item_name;
        this.item_id=item_id;
        this.price=price;
        this.categories = categories;
        this.sales=sales;
        this.user_name=user_name;
        this.thumbnail = thumbnail;
    }

    public String getItemName() {
        return item_name;
    }
    public void setItemName(String item_name) {
        this.item_name = item_name;
    }

    public int getItemId() {
        return item_id;
    }
    public void setItemId(int item_id) {
        this.item_id = item_id;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategories() {
        return categories;
    }
    public void setCategories(String categories) {
        this.categories = categories;
    }


    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSales() {
        return sales;
    }
    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


}
