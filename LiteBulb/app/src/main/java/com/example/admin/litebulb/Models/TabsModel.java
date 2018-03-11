package com.example.admin.litebulb.Models;

public class TabsModel {

    private String metaTitle;
    private String subOf;
    private String categoryId;



    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public TabsModel(String categoryId) {
        this.categoryId = categoryId;
    }

    public TabsModel(){

    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }


    public String getSubOf() {
        return subOf;
    }

    public void setSubOf(String subOf) {
        this.subOf = subOf;
    }

    public TabsModel(String metaTitle, String subOf) {
        this.metaTitle = metaTitle;
        this.subOf = subOf;
    }
}