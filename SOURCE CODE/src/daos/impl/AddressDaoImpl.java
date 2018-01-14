package daos.impl;

import daos.AddressDao;
import db.DBManager;
import main.Address;
import main.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressDaoImpl implements AddressDao {
    private Connection con;

    public AddressDaoImpl() {
        this.con = DBManager.getCon();
    }


    @Override
    public boolean addAddress(int userID, String firstname, String lastname, String address, String city, String zip, String state, String phonenumber) {
        try {
            // se la password attuale coincide posso aggiornare il campo con la nuova password
            PreparedStatement stm = this.con.prepareStatement("INSERT INTO shippingaddress (UserID,FirstName,LastName,Address,City,ZIP,State,TelephoneNumber,Active)\n" +
                    "VALUES(?,?,?,?,?,?,?,?,1) ");
            stm.setInt(1, userID);
            stm.setString(2, firstname);
            stm.setString(3, lastname);
            stm.setString(4, address);
            stm.setString(5, city);
            stm.setString(6, zip);
            stm.setString(7, state);
            stm.setString(8, phonenumber);
            int i = stm.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Address getAddress(int addressID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM shippingaddress WHERE AddressID = ?");
            stm.setInt(1, addressID);
            ResultSet rs = stm.executeQuery();
            System.out.println("");
            Address address = extractAddressFromResultSet(rs);
            System.out.println("[ INFO ] " + (address != null ? address.toString() : "Address is NULL"));
            return address;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<Address> getAllAddresses(User user) {
        ArrayList<Address> addresses = new ArrayList<>();
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * \n" +
                    "FROM shippingaddress \n" +
                    "WHERE UserID = ? AND Active = 1");
            stm.setInt(1, user.getUserID());
            ResultSet rs = stm.executeQuery();
            while (true){
                Address a = extractAddressFromResultSet(rs);
                if (a == null){
                    break;
                }
                addresses.add(a);
            }
            return addresses;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addresses;
    }

    @Override
    public boolean removeAddress(int addressID, int userID) {
        try {
            // se la password attuale coincide posso aggiornare il campo con la nuova password
            PreparedStatement stm = this.con.prepareStatement("UPDATE shippingaddress\n" +
                    "SET Active = 0\n" +
                    "WHERE AddressID = ? AND UserID = ?");
            stm.setInt(1, addressID);
            stm.setInt(2, userID);
            int i = stm.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean editAddress(User user, String addressID, String firstName, String lastName, String address, String city, String zip, String state, String phone) {
        if(!addressHasOrders(addressID)) {
            try {
                // se la password attuale coincide posso aggiornare il campo con la nuova password
                PreparedStatement stm = this.con.prepareStatement("UPDATE shippingaddress \n" +
                        "SET FirstName = ?, LastName = ?, Address = ?, City = ?, ZIP = ?, State = ?, TelephoneNumber = ?\n" +
                        "WHERE AddressID = ? AND UserID = ?");
                stm.setString(1, firstName);
                stm.setString(2, lastName);
                stm.setString(3, address);
                stm.setString(4, city);
                stm.setString(5, zip);
                stm.setString(6, state);
                stm.setString(7, phone);
                stm.setString(8, addressID);
                stm.setInt(9, user.getUserID());
                stm.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(addAddress(user.getUserID(),firstName,lastName,address,city,zip,state,phone)){
            return removeAddress(Integer.parseInt(addressID),user.getUserID());
        }
        return false;
    }

    private boolean addressHasOrders(String addressID) {
        try {
            // se la password attuale coincide posso aggiornare il campo con la nuova password
            PreparedStatement stm = con.prepareStatement("SELECT *\n" +
                    "FROM orderprod\n" +
                    "WHERE AddressID = ?");
            stm.setString(1, addressID);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // se va storto qualcosa nel dubbio meglio fare una nuova entry
        return true;
    }

    private Address extractAddressFromResultSet(ResultSet rs) throws SQLException {
        if(!rs.next()){
            return null;
        }
        Address address = new Address();
        address.setAddressID(rs.getInt("AddressID"));
        address.setUserID(rs.getInt("UserID"));
        address.setAddress(rs.getString("Address"));
        address.setFirstName(rs.getString("FirstName"));
        address.setLastName(rs.getString("LastName"));
        address.setCity(rs.getString("City"));
        address.setZip(rs.getString("ZIP"));
        address.setState(rs.getString("State"));
        address.setTelephoneNumber(rs.getString("TelephoneNumber"));
        address.setActive(rs.getInt("Active"));
        return address;
    }
}
