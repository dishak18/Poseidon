package com.example.admin.litebulb.Models;

public class CategoryItem {
    String name;
    String byInfo;
    String price;
    String category;
    String itemId;
    String thumbnail;

    public CategoryItem(){}

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public CategoryItem(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getByInfo() {
        return byInfo;
    }

    public void setByInfo(String byInfo) {
        this.byInfo = byInfo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public CategoryItem(String name, String byInfo, String price, String category, String thumbnail) {
        this.name = name;
        this.byInfo = byInfo;
        this.price = price;
        this.category = category;
        this.thumbnail=thumbnail;
    }
}