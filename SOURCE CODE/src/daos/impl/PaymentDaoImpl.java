package daos.impl;

import daos.PaymentDao;
import db.DBManager;
import main.User;

import java.sql.*;

public class PaymentDaoImpl implements PaymentDao {
    private Connection con;

    public PaymentDaoImpl() {
        this.con = DBManager.getCon();
    }

    @Override
    public int createPayment(User user, String cardHolder, String cardNumber, String expiryDate, int cvv) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO userpayment (UserID, CardHolder, CardNumber, ExpiryDate, Cvv)  VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, user.getUserID());
            stm.setString(2, cardHolder);
            stm.setString(3, cardNumber);
            stm.setString(4, expiryDate);
            stm.setInt(5, cvv);
            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
