package daos.impl;

import daos.NotificationDao;
import db.DBManager;
import main.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class NotificationDaoImpl implements NotificationDao {
    private Connection con;

    private boolean DEBUG = false;

    public NotificationDaoImpl() {
        this.con = DBManager.getCon();
    }

    @Override
    public boolean createProductReviewNotification(int reviewID, String title, int rating) {
        ArrayList<Integer> shops = new ArrayList<>();
        try {
            PreparedStatement stm = con.prepareStatement("SELECT sp.ShopID\n" +
                    "FROM productreview pr\n" +
                    "INNER JOIN shopproduct sp USING (ProductID)\n" +
                    "WHERE ReviewID = ?");
            stm.setInt(1, reviewID);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                shops.add(rs.getInt("ShopID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            for (int shopID : shops) {
                PreparedStatement stm = con.prepareStatement("INSERT INTO productreviewnotification (ReviewID, ShopID, Title, Rating) VALUES (?,?,?,?)");
                stm.setInt(1, reviewID);
                stm.setInt(2, shopID);
                stm.setString(3, title);
                stm.setInt(4, rating);
                int result = stm.executeUpdate();
                if (result == 0) {
                    return false;
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createDisputeNotification(String title, int orderID, int productID, int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO disputenotification (Title, OrderID, ProductID, ShopID, AdminStatus, ShopStatus) VALUES (?,?,?,?,0,0)");
            stm.setString(1, title);
            stm.setInt(2, orderID);
            stm.setInt(3, productID);
            stm.setInt(4, shopID);

            int result = stm.executeUpdate();
            return result != 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<Notification> getVendorNotifications(User user) {
        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.addAll(getProductReviewNotifications(new UserDaoImpl().getShopID(user)));
        notifications.addAll(getShopReviewNotifications(new UserDaoImpl().getShopID(user)));
        notifications.addAll(getDisputeNotifications(new UserDaoImpl().getShopID(user)));
        Comparator<Notification> dateComparator = Comparator.comparing(Notification::getCreationDate);
        notifications.sort(dateComparator.reversed());
        printNotifications(notifications, DEBUG);
        return notifications;
    }

    @Override
    public ArrayList<Notification> getAdminNotifications() {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT *, p.Name AS ProductName, s.Name AS ShopName " +
                    "FROM disputenotification d, product p, shop s " +
                    "WHERE d.ShopID = s.ShopID AND d.ProductID = p.ProductID " +
                    "ORDER BY CreationDate DESC");
            ResultSet rs = stm.executeQuery();
            ArrayList<Notification> notifications = extractDisputeNotificationFromResultSet(rs);
            printNotifications(notifications, DEBUG);
            return notifications;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Notification> getProductReviewNotifications(int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM productreviewnotification WHERE ShopID = ? ORDER BY CreationDate DESC");
            stm.setInt(1, shopID);
            ResultSet rs = stm.executeQuery();
            ArrayList<Notification> notifications = extractProductReviewNotificationFromResultSet(rs);
            printNotifications(notifications, DEBUG);
            return notifications;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Notification> getShopReviewNotifications(int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM shopreviewnotification WHERE ShopID = ? ORDER BY CreationDate DESC");
            stm.setInt(1, shopID);
            ResultSet rs = stm.executeQuery();
            ArrayList<Notification> notifications = extractShopReviewNotificationFromResultSet(rs);
            printNotifications(notifications, DEBUG);
            return notifications;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Notification> getDisputeNotifications(int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM disputenotification WHERE ShopID = ? ORDER BY CreationDate DESC");
            stm.setInt(1, shopID);
            ResultSet rs = stm.executeQuery();
            ArrayList<Notification> notifications = extractDisputeNotificationFromResultSet(rs);
            printNotifications(notifications, DEBUG);
            return notifications;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean readNotifications(User user) {
        boolean result = true;
        if (user.getType() == 1) { // user is admin
            try {
                PreparedStatement stm = con.prepareStatement("UPDATE disputenotification\n" +
                        "SET AdminStatus = 1");
                stm.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }
        }
        if (user.hasShop() && result) { // user is vendor and if he is admin the part before has succeed
            try {
                PreparedStatement stm = con.prepareStatement("UPDATE productreviewnotification\n" +
                        "SET ShopStatus = 1\n" +
                        "WHERE ShopID = ?");
                stm.setInt(1, user.getUserID());
                stm.executeUpdate();

                stm = con.prepareStatement("UPDATE shopreviewnotification\n" +
                        "SET ShopStatus = 1\n" +
                        "WHERE ShopID = ?");
                stm.setInt(1, user.getUserID());
                stm.executeUpdate();

                stm = con.prepareStatement("UPDATE disputenotification\n" +
                        "SET ShopStatus = 1\n" +
                        "WHERE ShopID = ?");
                stm.setInt(1, user.getUserID());
                stm.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }
        }

        return result;
    }

    private ArrayList<Notification> extractProductReviewNotificationFromResultSet(ResultSet rs) {
        ArrayList<Notification> notifications = new ArrayList<>();

        try {
            while (rs.next()) {
                NotificationProductReview n = new NotificationProductReview();
                n.setTitle(rs.getString("Title"));
                n.setShopId(rs.getInt("ShopID"));
                n.setCreationDate(rs.getTimestamp("CreationDate"));
                n.setShopStatus(rs.getInt("ShopStatus"));
                n.setRating(rs.getInt("Rating"));

                notifications.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    private ArrayList<Notification> extractShopReviewNotificationFromResultSet(ResultSet rs) {
        ArrayList<Notification> notifications = new ArrayList<>();

        try {
            while (rs.next()) {
                NotificationShopReview n = new NotificationShopReview();
                n.setTitle(rs.getString("Title"));
                n.setShopId(rs.getInt("ShopID"));
                n.setCreationDate(rs.getTimestamp("CreationDate"));
                n.setShopStatus(rs.getInt("ShopStatus"));
                n.setUserID(rs.getInt("UserID"));
                n.setRating(rs.getInt("Rating"));

                notifications.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    private ArrayList<Notification> extractDisputeNotificationFromResultSet(ResultSet rs) {
        ArrayList<Notification> notifications = new ArrayList<>();

        try {
            while (rs.next()) {
                NotificationDispute n = new NotificationDispute();
                n.setTitle(rs.getString("Title"));
                n.setShopId(rs.getInt("ShopID"));
                try {
                    n.setShopName(rs.getString("ShopName"));
                    n.setProductName(rs.getString("ProductName"));
                } catch (Exception e) {

                }
                n.setCreationDate(rs.getTimestamp("CreationDate"));
                n.setShopStatus(rs.getInt("ShopStatus"));
                n.setAdminStatus(rs.getInt("AdminStatus"));
                n.setOrderId(rs.getInt("OrderID"));
                n.setProductId(rs.getInt("ProductID"));

                notifications.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    private void printNotifications(ArrayList<Notification> a, boolean DEBUG) {
        if (!DEBUG)
            return;
        for (Notification n : a) {
            System.out.println("Notification:");
            System.out.println("    Creation date: " + n.getCreationDate());
            System.out.println("    Shop ID: " + n.getShopId());
            System.out.println("    Shop Status: " + n.getShopStatus());
            System.out.flush();
        }
    }

    public boolean createShopReviewNotification(int userID, int shopID, String title, int rating) {

        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO shopreviewnotification (ShopID, UserID, Title, Rating) VALUES (?,?,?,?)");
            stm.setInt(1, shopID);
            stm.setInt(2, userID);
            stm.setString(3, title);
            stm.setInt(4, rating);
            int result = stm.executeUpdate();
            if (result == 0) {
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
