package com.star.market.europeanstarmarket.models;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by rohai on 11/11/2016.
 */

public class MainCategoryModel implements Serializable {

    private String id;
    private String name;
    private String subCatCount;
    private String image;

    @Override
    public String toString() {
        return "MainCategoryModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", subCatCount='" + subCatCount + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubCatCount() {
        return subCatCount;
    }

    public void setSubCatCount(String subCatCount) {
        this.subCatCount = subCatCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
