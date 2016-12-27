package com.star.market.europeanstarmarket.models;

import java.io.Serializable;

/**
 * Created by rohail on 11/14/2016.
 */

public class SubProductModel implements Serializable {

    private String Id;
    private String Name;
    private String imagelink;
    private String price;

    @Override
    public String toString() {
        return "SubProductModel{" +
                "Id='" + Id + '\'' +
                ", Name='" + Name + '\'' +
                ", imagelink='" + imagelink + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
