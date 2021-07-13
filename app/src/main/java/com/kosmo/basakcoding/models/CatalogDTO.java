package com.kosmo.basakcoding.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class CatalogDTO {
    @SerializedName("courseId")
    private Integer courseId;

    @SerializedName("adminId")
    private Integer adminId;

    @SerializedName("categoryId")
    private Integer categoryId;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("name")
    private String name;

    @SerializedName("title")
    private String title;

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("difficulty")
    private String difficulty;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updateAt")
    private Date updateAt;

    @SerializedName("period")
    private String period;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
