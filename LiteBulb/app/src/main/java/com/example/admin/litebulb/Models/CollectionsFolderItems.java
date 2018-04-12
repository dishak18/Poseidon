package com.example.admin.litebulb.Models;

public class CollectionsFolderItems{
    private String categories, sales_items, item_name, user_name;
    private int item_id, price;
    private String thumbnail;
    public CollectionsFolderItems(){

    }
    public CollectionsFolderItems(int item_id, int price, String item_name, String categories, String sales_items, String user_name, String thumbnail) {
        this.item_name = item_name;
        this.item_id=item_id;
        this.price=price;
        this.categories = categories;
        this.sales_items=sales_items;
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

    public String getSales_items() {
        return sales_items;
    }
    public void setSales_items(String sales_items) {
        this.sales_items = sales_items;
    }

    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


}
