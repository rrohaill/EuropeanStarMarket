package com.star.market.europeanstarmarket.models;

/**
 * Created by rohail on 11/19/2016.
 */

public class CheckoutModel {

    private String id;
    private String price;
    private String quantity;

    @Override
    public String toString() {
        return "CheckoutModel{" +
                "id='" + id + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
