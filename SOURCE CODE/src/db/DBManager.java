package db;

import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import main.ProductGroup;
import main.Shop;
import main.Product;
import utils.PropertiesReader;

import static utils.Mechanist.*;

public class DBManager implements Serializable {
    private static transient Connection con;

    public static Connection getCon() {
        return con;
    }

    public DBManager() throws SQLException {
        String database, user, password, timezone_fix;
        try {
            PropertiesReader pr = new PropertiesReader("config.properties");
            database = pr.get("database");
            user = pr.get("user");
            password = pr.get("password");
            timezone_fix = "?useUnicode=true" +
                    "&useJDBCCompliantTimezoneShift=true" +
                    "&useLegacyDatetimeCode=false" +
                    "&serverTimezone=UTC" +
                    "&useSSL=false" +
                    "&useUnicode=true" +
                    "&characterEncoding=UTF-8";
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }

        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + database + timezone_fix + "&user=" + user + "&password=" + password);
    }

    public static void shutdown() { //static?
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).info(ex.getMessage());
        }
    }

    public static ArrayList<String> getCategories(Map params) throws SQLException {
        ArrayList<String> ret = new ArrayList<String>();

        String searchQuery;
        if((searchQuery = checkSMP(params.get("q"))) == null){
            return null;
        }

        //Query PreparedStatement
        PreparedStatement stm = null;
        stm = con.prepareStatement(
                "SELECT DISTINCT P.CategoryName " +
                        "FROM Product P, ShopProduct SP, Shop S, ShopInfo SI " +
                        "WHERE P.Name LIKE ? AND P.ProductID = SP.ProductID AND SP.ShopID = S.ShopID AND SP.Quantity > 0 "
        );
        stm.setString(1,"%"+searchQuery+"%");
        System.out.println("SIDEBAR GET CATEGORY: " + stm.toString().substring(45));

        //Execution
        try (ResultSet rs = stm.executeQuery()){
            while(rs.next()) {
                ret.add(rs.getString("CategoryName"));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
            stm.close();
        }

        return ret;
    }

    public static ArrayList<String> getVendors(Map params) throws SQLException {
        ArrayList<String> ret = new ArrayList<String>();

        String searchQuery;
        if((searchQuery = checkSMP(params.get("q"))) == null){
            return null;
        }

        //Query PreparedStatement
        PreparedStatement stm = null;
        stm = con.prepareStatement(
                "SELECT DISTINCT S.Name " +
                        "FROM Product P, ShopProduct SP, Shop S, ShopInfo SI " +
                        "WHERE P.Name LIKE ? AND P.ProductID = SP.ProductID AND SP.ShopID = S.ShopID AND SP.Quantity > 0 "
        );
        stm.setString(1,"%"+searchQuery+"%");
        System.out.println("SIDEBAR GET VENDORS: " + stm.toString().substring(45));

        //Execution
        try (ResultSet rs = stm.executeQuery()){
            while(rs.next()) {
                ret.add(rs.getString("Name"));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
            stm.close();
        }

        return ret;
    }

    public static ArrayList<String> getGeoZone(Map params) throws SQLException {
        ArrayList<String> ret = new ArrayList<String>();

        String searchQuery;
        if((searchQuery = checkSMP(params.get("q"))) == null){
            return null;
        }

        //Query PreparedStatement
        PreparedStatement stm = con.prepareStatement(
                "SELECT DISTINCT SI.State " +
                        "FROM Product P, ShopProduct SP, Shop S, ShopInfo SI " +
                        "WHERE P.Name LIKE ? AND P.ProductID = SP.ProductID AND SP.ShopID = S.ShopID AND SP.Quantity > 0 "
        );
        stm.setString(1,"%"+searchQuery+"%");
        System.out.println("SIDEBAR GET GEOZONE: " + stm.toString().substring(45));

        //Execution
        try (ResultSet rs = stm.executeQuery()){
            while(rs.next()) {
                ret.add(rs.getString("State"));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
            stm.close();
        }
        return ret;
    }
}