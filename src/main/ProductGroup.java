package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductGroup implements Serializable {

    private List<Product> list;
    private int reviewCount = 0;
    private String imageData = "";
    private List<Shop> vendors;
    private ArrayList<String> geo;
    private double lastDistance;

    public ProductGroup(){
        list = new ArrayList<>();
        vendors = new ArrayList<>();
        geo = new ArrayList<>();
        lastDistance = 0;
    }

    public List<Product> getList() {
        return list;
    }

    public float getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public List<Shop> getVendors() {
        return vendors;
    }

    public ArrayList<String> getGeo() {
        return geo;
    }

    public double getLastDistance() {
        return lastDistance;
    }

    public void setLastDistance(double lastDistance) {
        this.lastDistance = lastDistance;
    }
}
