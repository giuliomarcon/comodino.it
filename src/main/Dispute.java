package main;

import java.sql.Timestamp;

public class Dispute {
  private String title;
  private int status;
  private String description;
  private Timestamp creationDate;
  private int orderID;
  private int productID;
  private int shopID;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Timestamp creationDate) {
    this.creationDate = creationDate;
  }

  public int getOrderID() {
    return orderID;
  }

  public void setOrderID(int orderID) {
    this.orderID = orderID;
  }

  public int getProductID() {
    return productID;
  }

  public void setProductID(int productID) {
    this.productID = productID;
  }

  public int getShopID() {
    return shopID;
  }

  public void setShopID(int shopID) {
    this.shopID = shopID;
  }
}
