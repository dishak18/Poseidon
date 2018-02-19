package com.example.admin.litebulb.Models;

/**
 * Created by grandiose on 13/2/18.
 */

public class MoreMainCategory {
    String mainCategory;
    String mainCategoryId;

    public MoreMainCategory(){

    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public MoreMainCategory(String mainCategory, String mainCategoryId) {
        this.mainCategory = mainCategory;
        this.mainCategoryId = mainCategoryId;
    }


}