package daos.impl;

import daos.DisputeDao;
import db.DBManager;
import main.Dispute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DisputeDaoImpl implements DisputeDao {
    private Connection con;

    public DisputeDaoImpl() {
        this.con = DBManager.getCon();
    }

    @Override
    public boolean createDispute(int orderID, int productID, int shopID, String title, String description) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO dispute (Title, Status, Description, OrderID, ProductID, ShopID) VALUES (?,0,?,?,?,?)");
            stm.setString(1, title);
            stm.setString(2, description);
            stm.setInt(3, orderID);
            stm.setInt(4, productID);
            stm.setInt(5, shopID);

            int result = stm.executeUpdate();
            return result != 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<Dispute> allDisputes(){

        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM dispute ORDER BY CreationDate DESC");
            ResultSet rs = stm.executeQuery();
            ArrayList<Dispute> disputes = extractDisputesFromResultSet(rs);
            for (Dispute d:disputes
                 ) {
                System.out.println("d: " + d.getTitle());
            }
            return disputes;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Dispute> extractDisputesFromResultSet(ResultSet rs) {
        ArrayList<Dispute> disputes = new ArrayList<>();

        try {
            while (rs.next()){
                Dispute d = new Dispute();
                d.setTitle(rs.getString("Title"));
                d.setStatus(rs.getInt("Status") );
                d.setDescription(rs.getString("Description"));
                d.setCreationDate(rs.getTimestamp("CreationDate"));
                d.setOrderID(rs.getInt("OrderID"));
                d.setProductID(rs.getInt("ProductID"));
                d.setShopID(rs.getInt("ShopID"));

                disputes.add(d);
                System.out.println("d: " + d.getTitle());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disputes;
    }

    private Dispute extractDisputeFromResultSet(ResultSet rs) {
        try {
            if (rs.next()){
                Dispute d = new Dispute();
                d.setTitle(rs.getString("Title"));
                d.setStatus(rs.getInt("Status") );
                d.setDescription(rs.getString("Description"));
                d.setCreationDate(rs.getTimestamp("CreationDate"));
                d.setOrderID(rs.getInt("OrderID"));
                d.setProductID(rs.getInt("ProductID"));
                d.setShopID(rs.getInt("ShopID"));
                System.out.println("DISPUTA TROVATA -------------------------------------");
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateDispute(int orderID, int productID, int shopID, int status) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE dispute\n" +
                    "SET Status = ?\n" +
                    "WHERE OrderID = ? AND ProductID = ? AND ShopID = ?;");
            stm.setInt(1, status);
            stm.setInt(2, orderID);
            stm.setInt(3, productID);
            stm.setInt(4, shopID);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Dispute getDisputeByUser(int orderID, int productID, int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * " +
                    "FROM dispute " +
                    "WHERE OrderID = ? AND ProductID = ? AND ShopID = ?");
            stm.setInt(1, orderID);
            stm.setInt(2, productID);
            stm.setInt(3, shopID);
            ResultSet rs = stm.executeQuery();
            return extractDisputeFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Dispute> getDisputeByShop(int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * " +
                    "FROM dispute d " +
                    "WHERE d.ShopID = ? " +
                    "ORDER BY d.CreationDate DESC");
            stm.setInt(1, shopID);
            ResultSet rs = stm.executeQuery();
            return extractDisputesFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
