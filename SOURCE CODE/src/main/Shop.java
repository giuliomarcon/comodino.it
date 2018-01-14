package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Shop implements Serializable {

    private int shopID = 0;
    private String name = "";
    private String description = "";
    private String website = "";
    private float rating = -1;
    private float sampleActualPrice = 0; //ovviamente uno shop non ha un prezzo singolo, ma ci serve per il modal nella ricerca

    private ArrayList<String> shopphoto = new ArrayList<>();

    private ArrayList<Product> expiringProducts = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getSampleActualPrice() {
        return sampleActualPrice;
    }

    public void setSampleActualPrice(float sampleActualPrice) {
        this.sampleActualPrice = sampleActualPrice;
    }


    public ArrayList<String> getShopphoto() {
        return shopphoto;
    }

    public void setShopphoto(ArrayList<String> shopphoto) {
        this.shopphoto = shopphoto;
    }

    public ArrayList<Product> getExpiringProducts() {
        return expiringProducts;
    }

    public void setExpiringProducts(ArrayList<Product> expiringProducts) {
        this.expiringProducts = expiringProducts;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
