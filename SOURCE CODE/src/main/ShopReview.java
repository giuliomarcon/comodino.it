package main;

public class ShopReview {
  private String title;
  private java.sql.Timestamp creationdate;
  private float rating;
  private String description;
  private int shopID;
  private int userID;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public java.sql.Timestamp getCreationDate() {
    return creationdate;
  }

  public void setCreationDate(java.sql.Timestamp creationdate) {
    this.creationdate = creationdate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }

  public int getShopID() {
    return shopID;
  }

  public void setShopID(int shopID) {
    this.shopID = shopID;
  }

  public int getUserID() {
    return userID;
  }

  public void setUserID(int userID) {
    this.userID = userID;
  }
}
