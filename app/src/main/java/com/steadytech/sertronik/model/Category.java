package com.steadytech.sertronik.model;

public class Category {
    String categoryName, categoryCode, imageCategoryPath;

    public Category() {
    }

    public Category(String categoryName, String categoryCode, String imageCategoryPath) {
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.imageCategoryPath = imageCategoryPath;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getImageCategoryPath() {
        return imageCategoryPath;
    }

    public void setImageCategoryPath(String imageCategoryPath) {
        this.imageCategoryPath = imageCategoryPath;
    }
}
