package com.library.model;

public class Category {
    private int categoryId;      // NUMBER(4)
    private String categoryName; // VARCHAR2(30)


    // Getters and Setters 保持不变
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}