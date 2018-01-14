package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {
    private int orderID = 0;
    private int userID = 0;
    private Date date = null;
    private int paymentID = 0;
    private ArrayList<ProdOrder> productList = new ArrayList<>();

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public ArrayList<ProdOrder> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProdOrder> productList) {
        this.productList = productList;
    }

    public float getTotal() {
        float total = 0;
        for (ProdOrder po:this.productList) {
            float price = po.getFinalPrice();
            int quantity = po.getQuantity();
            total += quantity*price;
        }
        return total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", userID=" + userID +
                ", date=" + date +
                ", paymentID=" + paymentID +
                ", productList=" + productList +
                '}';
    }

    public boolean isFinalized() {
        for (ProdOrder product : productList) {
            if (product.getStatus() == 0) return false;
        }
        return true;
    }
}

