package daos.impl;

import daos.ReviewDao;
import db.DBManager;
import main.*;

import java.sql.*;
import java.util.ArrayList;

public class ReviewDaoImpl implements ReviewDao {

    private Connection con;

    public ReviewDaoImpl() {
        this.con = DBManager.getCon();
    }

    @Override
    public int createProductReview(String title, String description, int rating, int productID, int userID) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO productreview (Title, Description, Rating, ProductID, UserID, CreationDate) \n" +
                    "VALUES (?,?,?,?,?,NOW())", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, title);
            stm.setString(2, description);
            stm.setInt(3, rating);
            stm.setInt(4, productID);
            stm.setInt(5, userID);
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys(); // bisogna riferirsi ai campi con il numero in sto caso
            if (rs.next()){
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int createProductReviewReply(String description, int reviewID) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO reviewreply (Description, ReviewID)\n" +
                    "VALUES (?,?)");
            stm.setString(1, description);
            stm.setInt(2, reviewID);
            int res = stm.executeUpdate();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int isReviewReplied(int reviewID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT COUNT(*) AS reviewcount\n" +
                    "FROM reviewreply rr\n" +
                    "WHERE rr.ReviewID = ?");
            stm.setInt(1, reviewID);

            try (ResultSet rs = stm.executeQuery()) {
                rs.next();
                //System.out.println("REVIEW REPLY COUNT: " + rs.getInt("reviewcount"));
                int res = rs.getInt("reviewcount");
                return res;
            } finally {
                stm.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ReviewReply getProductReviewReply(int reviewID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT *\n" +
                    "FROM reviewreply rr\n" +
                    "WHERE rr.ReviewID = ?");
            stm.setInt(1, reviewID);
            ResultSet rs = stm.executeQuery();
            return extractProductReviewReplyFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ProductReview> getProductReviews(int productID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * " +
                    "FROM productreview " +
                    "WHERE ProductID = ? " +
                    "ORDER BY CreationDate DESC");
            stm.setInt(1, productID);
            ResultSet rs = stm.executeQuery();
            return extractProductReviewsFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int createShopReview(String title, String description, int rating, int shopID, int userID) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO shopreview (Title, CreationDate, Rating, Description, ShopID, UserID) VALUES (?,NOW(),?,?,?,?)");
            stm.setString(1, title);
            stm.setString(3, description);
            stm.setInt(2, rating);
            stm.setInt(4, shopID);
            stm.setInt(5, userID);
            stm.executeUpdate();
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ArrayList<ShopReview> getShopReviews(int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM shopreview sr WHERE sr.ShopID = ? ORDER BY sr.CreationDate DESC");
            stm.setInt(1, shopID);
            ResultSet rs = stm.executeQuery();
            return extractShopReviewsFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ProductReview> getVendorProductReviews(int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT *\n" +
                    "FROM productreview\n" +
                    "WHERE ProductID IN (SELECT DISTINCT P.ProductID\n" +
                    "                    FROM Product P, ShopProduct SP, Shop S, ShopInfo SI\n" +
                    "                    WHERE P.ProductID = SP.ProductID AND SP.ShopID = S.ShopID AND S.ShopID = ? )" +
                    "ORDER BY productreview.CreationDate DESC");
            stm.setInt(1, shopID);
            ResultSet rs = stm.executeQuery();
            return extractProductReviewsFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<ProductReview> extractProductReviewsFromResultSet(ResultSet rs) {
        ArrayList<ProductReview> reviewList = new ArrayList<>();

        try {
            ProductReview productReview;

            while (rs.next()) {

                // creo inserisco dati ordine generale
                productReview = new ProductReview();

                productReview.setCreationDate(rs.getTimestamp("CreationDate"));
                productReview.setDescription(rs.getString("Description"));
                productReview.setRating(rs.getFloat("Rating"));
                productReview.setTitle(rs.getString("Title"));
                productReview.setUserID(rs.getInt("UserID"));
                productReview.setProductID(rs.getInt("ProductID"));
                productReview.setReviewID(rs.getInt("ReviewID"));


                // aggiungo l'ordine del prodotto al corrispettivo ordine generale
                reviewList.add(productReview);

            }

            return reviewList;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    private ArrayList<ShopReview> extractShopReviewsFromResultSet(ResultSet rs) {
        ArrayList<ShopReview> reviewList = new ArrayList<>();

        try {
            ShopReview shopReview;

            while (rs.next()) {

                shopReview = new ShopReview();

                shopReview.setTitle(rs.getString("Title"));
                shopReview.setCreationDate(rs.getTimestamp("CreationDate"));
                shopReview.setRating(rs.getFloat("Rating"));
                shopReview.setDescription(rs.getString("Description"));
                shopReview.setShopID(rs.getInt("ShopID"));
                shopReview.setUserID(rs.getInt("UserID"));

                reviewList.add(shopReview);
            }

            return reviewList;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }


    private ProductReview extractProductReviewFromResultSet(ResultSet rs) {
        try {
            if (rs.next()){
                ProductReview productReview = new ProductReview();

                productReview.setCreationDate(rs.getTimestamp("CreationDate"));
                productReview.setDescription(rs.getString("Description"));
                productReview.setRating(rs.getFloat("Rating"));
                productReview.setTitle(rs.getString("Title"));
                productReview.setUserID(rs.getInt("UserID"));
                productReview.setProductID(rs.getInt("ProductID"));
                productReview.setReviewID(rs.getInt("ReviewID"));
                return productReview;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ReviewReply extractProductReviewReplyFromResultSet(ResultSet rs) {
        try {
            if (rs.next()){
                ReviewReply reviewReply = new ReviewReply();

                reviewReply.setDescription(rs.getString("Description"));
                reviewReply.setReviewID(rs.getInt("ReviewID"));
                return reviewReply;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getReviewAuthor(int userID){
        return new UserDaoImpl().getUser(userID);
    }

    public Product getReviewProduct(int productID, int shopID){ return new ProductDaoImpl().getProduct(productID, shopID);}

    @Override
    public ProductReview getProductReviewByUser(User user, int productID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * " +
                    "FROM productreview " +
                    "WHERE UserID = ? AND ProductID = ? " +
                    "ORDER BY CreationDate DESC");
            stm.setInt(1, user.getUserID());
            stm.setInt(2, productID);
            ResultSet rs = stm.executeQuery();
            return extractProductReviewFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
