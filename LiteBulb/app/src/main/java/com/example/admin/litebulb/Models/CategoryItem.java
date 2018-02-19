package com.example.admin.litebulb.Models;

public class CategoryItem {
    String name;
    String byInfo;
    String price;
    String category;
    String itemId;

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

    public CategoryItem(String name, String byInfo, String price, String category) {
        this.name = name;
        this.byInfo = byInfo;
        this.price = price;
        this.category = category;
    }
}