package main;

import daos.UserDao;
import daos.impl.UserDaoImpl;

import java.io.Serializable;

public class User implements Serializable {

    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private int type; // 0 normale o venditore, 1 admin
    private int shopID; // 0 se non è venditore, shopID se è venditore
    private int privacy;
    private Cart cart;
    private String profilePhoto;

    public User() {
        updateShopID();
        updateCart();
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean hasShop() {
        return this.shopID != 0;
    }

    public int getShopID() {
        return this.shopID;
    }

    public void updateShopID() {
        this.shopID = new UserDaoImpl().getShopID(this);
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public Cart getCart() {
        this.updateCart();
        return cart;
    }

    public Cart getCart(boolean update) {
        if(update)
            this.updateCart();
        return cart;
    }

    public void updateCart() {
        //System.out.println("[INFO] Cart Updated");
        System.out.flush();
        this.cart = new UserDaoImpl().getCart(this);
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public boolean hasReviewedShop(int shopID) {
        UserDao userDao = new UserDaoImpl();
        return userDao.checkIfReviewExists(this.userID, shopID);
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}