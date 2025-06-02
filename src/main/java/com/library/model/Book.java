package com.library.model;

import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Book {
    // 核心字段（严格对应BOOKS表）
    private String isbn;          // VARCHAR2(20)
    private String title;         // VARCHAR2(100)
    private String author;        // VARCHAR2(50)
    private Integer categoryId;   // NUMBER(4)
    private Integer publisherId;  // NUMBER(4)
    private Date publishDate;     // DATE
    private BigDecimal retailPrice; // NUMBER(8,2)
    private Integer stockQty;     // NUMBER(4)

    // 显示字段（关联查询结果）
    private String categoryName;  // CATEGORY.CATEGORYNAME
    private String publisherName; // PUBLISHERS.NAME

    // Getter/Setter（带数据校验）
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = (title != null) ? title.trim() : "";
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) {
        this.author = (author != null) ? author.trim() : "";
    }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;  // 直接赋值，不再过滤
    }

    public Integer getPublisherId() { return publisherId; }
    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;  // 直接赋值，不再过滤
    }

    public Date getPublishDate() { return publishDate; }
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public BigDecimal getRetailPrice() { return retailPrice; }
    public void setRetailPrice(BigDecimal retailPrice) {
        if (retailPrice != null) {
            this.retailPrice = retailPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            this.retailPrice = BigDecimal.ZERO; // 默认值处理
        }
    }

    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) {
        this.stockQty = (stockQty != null) ? stockQty : 0;
    }

    // 显示字段方法（空值安全）
    public String getCategoryName() {
        return (categoryName != null && !categoryName.isEmpty()) ? categoryName : "未分类";
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPublisherName() {
        return (publisherName != null && !publisherName.isEmpty()) ? publisherName : "未知出版社";
    }
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    // 格式化方法（供前端使用）
    public String getFormattedPublishDate() {
        if (publishDate != null) {
            return new SimpleDateFormat("yyyy-MM").format(publishDate);
        }
        return "未知日期";
    }

    public String getFormattedPrice() {
        return "¥" + retailPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return String.format("Book[ISBN=%s, Title=%s, Stock=%d]", isbn, title, stockQty);
    }
}