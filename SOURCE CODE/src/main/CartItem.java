package main;

import java.util.HashMap;

public class CartItem extends HashMap<String, Object> {

    public void put(Product product, Integer quantity, Integer address) {
        super.put("product", product);
        super.put("quantity", quantity);
        super.put("address", address);
    }
    public Product getProduct(){
        return (Product) super.get("product");
    }
    public Integer getQuantity(){
        return (Integer) super.get("quantity");
    }
    public Integer getAddress(){
        return (Integer) super.get("address");
    }
}
