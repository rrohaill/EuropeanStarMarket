package com.star.market.europeanstarmarket.models;

import java.io.Serializable;

/**
 * Created by rohail on 12/3/2016.
 */

public class HistoryModel implements Serializable {

    private String Name;

    private String price;

    private String qty;

    private String date;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "HistoryModel{" +
                "Name='" + Name + '\'' +
                ", price='" + price + '\'' +
                ", qty='" + qty + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
