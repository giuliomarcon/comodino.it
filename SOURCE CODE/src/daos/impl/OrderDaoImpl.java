package daos.impl;

import daos.AddressDao;
import daos.OrderDao;
import daos.ProductDao;
import db.DBManager;
import main.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderDaoImpl implements OrderDao {
    private Connection con;

    public OrderDaoImpl() {
        this.con = DBManager.getCon();
    }

    @Override
    public ArrayList<Order> getAllOrders(User user) {

        try {
            PreparedStatement stm = con.prepareStatement("SELECT * \n" +
                    "FROM orderprod \n" +
                    "INNER JOIN orderlist USING(OrderID)\n" +
                    "WHERE UserID = ? ORDER BY OrderID DESC");
            stm.setInt(1, user.getUserID());
            ResultSet rs = stm.executeQuery();
            //printRS(rs); // lasciare attiva solo per DEBUG altrimenti non trova gli ordini
            System.out.println("");
            ArrayList<Order> orders = extractProductFromResultSet(rs);
            assert orders != null;
            loadProductReviews(orders, user);
            loadDisputes(orders);
            System.out.println("Size: " + orders.size());
            return orders;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Order> getAllOrders(Shop shop) {

        try {
            PreparedStatement stm = con.prepareStatement("SELECT * \n" +
                    "FROM orderprod \n" +
                    "INNER JOIN orderlist USING(OrderID)\n" +
                    "WHERE ShopID = ? ORDER BY OrderID DESC");
            stm.setInt(1, shop.getShopID());
            ResultSet rs = stm.executeQuery();
            //printRS(rs); // lasciare attiva solo per DEBUG altrimenti non trova gli ordini
            System.out.println("");
            ArrayList<Order> orders = extractProductFromResultSet(rs);
            assert orders != null;
            System.out.println("Size: " + orders.size());
            return orders;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadDisputes(ArrayList<Order> orders) {
        for (Order o:orders){
            for (ProdOrder po:o.getProductList()){
                po.setDispute(new DisputeDaoImpl().getDisputeByUser(o.getOrderID(), po.getProduct().getProductID(), po.getProduct().getShopID()));
            }
        }
    }

    private void loadProductReviews(ArrayList<Order> orders, User user) {
        for (Order o:orders){
            for (ProdOrder po:o.getProductList()){
                po.setReview(new ReviewDaoImpl().getProductReviewByUser(user, po.getProduct().getProductID()));
            }
        }
    }

    @Override
    public boolean setOrderAddresses(User user, String address, ArrayList<String> shoppickup) {

        // setto tutti gli ordini dell'utente ad address
        if(!setShippingAddress(user, address)){
            return false;
        }
        // aggiorno indirizzo ritiro per tutti e soli quelli nella lista shoppickup
        for (String rit: shoppickup) {
            String[] prod_shop = rit.split("_");

            if (prod_shop.length != 2 || !setShopPickup(user, prod_shop[0], prod_shop[1])) {
                // errore inserimento nel database o stringa passata errata
                return false;
            }
        }


        return true;
    }

    @Override
    public int createOrder(User user, int paymentID) {
        Cart cart = user.getCart(true);
        int orderID;
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO orderlist (Date, UserID, PaymentID) VALUES (NOW(),?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, user.getUserID());
            stm.setInt(2, paymentID);
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();
            rs.next();
            orderID = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[ERROR] Impossibile creare entry orderlist");
            return 0;
        }

        try {
            PreparedStatement stm;
            for (CartItem item:cart){
                stm = con.prepareStatement("INSERT INTO orderprod (OrderID, ProductID, ShopID, Quantity, FinalPrice, AddressID) VALUES (?,?,?,?,?,?)");
                stm.setInt(1, orderID);
                stm.setInt(2, item.getProduct().getProductID());
                stm.setInt(3, item.getProduct().getShopID());
                stm.setInt(4, item.getQuantity());
                stm.setFloat(5, item.getProduct().getActualPrice());
                stm.setInt(6, item.getAddress());

                stm.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[ERROR] Impossibile creare entry orderprod");
            return 0;
        }
        return orderID;

    }

    @Override
    public boolean cleanCart(User user) {
        try {
            PreparedStatement stm = con.prepareStatement("DELETE FROM cart \n" +
                    "WHERE UserID = ?");
            stm.setInt(1, user.getUserID());
            stm.executeUpdate();
            System.out.println("[INFO] Cart cleaned");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean finishOrderProd(int userID, String orderID, String productID, String shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE orderprod SET Status = 1 \n" +
                    "WHERE OrderID IN (SELECT OrderID \n" +
                    "                  FROM orderlist \n" +
                    "                  WHERE UserID = ? AND orderlist.OrderID = ?) \n" +
                    "      AND ProductID = ? AND ShopID = ?\n");
            stm.setInt(1, userID);
            stm.setString(2, orderID);
            stm.setString(3, productID);
            stm.setString(4, shopID);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean setShippingAddress(User user, String address) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE cart SET AddressID = ? \n" +
                    "WHERE UserID = ?");
            stm.setString(1, address);
            stm.setInt(2, user.getUserID());
            stm.executeUpdate();
            System.out.println("[INFO] SetAddress: setted all addresses to address:" + address);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean setShopPickup(User user, String productID, String shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE cart SET AddressID = 0 \n" +
                    "WHERE UserID = ? AND ProductID = ? AND ShopID = ?");
            stm.setInt(1, user.getUserID());
            stm.setString(2, productID);
            stm.setString(3, shopID);
            stm.executeUpdate();
            System.out.println("[INFO] SetAddress: shop pick up setted for prod: " + productID + " shop: " + shopID);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void printRS(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }

    /**
     * La query del rs set deve essere per forza ordinata (ovvero prodotti con OrderID uguale sono tutti consecutivi)
     *
     * @param rs    ResultSet della query ORDINATA per OrderID
     * @return      Lista degli ordini
     */
    private ArrayList<Order> extractProductFromResultSet(ResultSet rs){
        ArrayList<Order> orderList = new ArrayList<>();

        try {
            Order order = new Order();
            ProdOrder ps;
            ProductDao pd;
            Product p;
            AddressDao ad;
            Address a;
            int i = 0;

            while (rs.next()){

                // creo inserisco dati ordine generale
                order = new Order();
                i++;
                order.setOrderID(rs.getInt("OrderID"));
                order.setUserID(rs.getInt("UserID"));
                order.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("Date")));
                //System.out.println("[ INFO ] Data: " + order.getDate().toString());
                // creo prodotto con dati venditore
                pd = new ProductDaoImpl();
                p = pd.getProduct(rs.getInt("ProductID"),rs.getInt("ShopID"));
                //System.out.println("[ INFO ] Prodotto : " + p.getProductName());

                // creo indirizzo spedizione
                ad = new AddressDaoImpl();
                a = ad.getAddress(rs.getInt("AddressID"));

                // creo l'ordine del prodotto particolare e lo aggiungo alla lista dell'ordine generale
                ps = extractProdOrder(rs,p,a);

                // aggiungo l'ordine del prodotto al corrispettivo ordine generale
                order.getProductList().add(ps);
                //System.out.println(order.toString());
                // ciclo sugli elementi successivi dell'ordine (basta aggiungere gli progelem alla lista ordine)
                while (rs.next()){

                    // se trovo un elemento che non appartiene più all'ordine corrente
                    if (rs.getInt("OrderID") != order.getOrderID()){
                        // finalizzo l'ordine
                        System.out.println("[ INFO ] Ordine " + order.getOrderID() + " aggiunto");
                        orderList.add(order);
                        // torno all'elemento precedente (perchè poi nel while esterno ritorno avanti di uno e dichiaro un nuovo ordine)
                        rs.previous();
                        // esco dal while interno (ovvero non ho più prodotti relativi all'ordine corrente)
                        break;
                    }

                    // creo prodotto con dati venditore
                    pd = new ProductDaoImpl();
                    p = pd.getProduct(rs.getInt("ProductID"), rs.getInt("ShopID"));
                    //System.out.println("[ INFO ] Prodotto : " + p.getProductName());

                    // creo indirizzo spedizione
                    ad = new AddressDaoImpl();
                    a = ad.getAddress(rs.getInt("AddressID"));

                    // creo l'ordine del prodotto particolare e lo aggiungo alla lista dell'ordine generale
                    ps = extractProdOrder(rs,p,a);

                    // aggiungo l'ordine del prodotto al corrispettivo ordine generale
                    order.getProductList().add(ps);
                }

            }
            if (i > 0){
                orderList.add(order);
                //System.out.println("[ INFO ] Ordine " + order.getOrderID() + " aggiunto");
            }

            return orderList;
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ProdOrder extractProdOrder(ResultSet rs, Product p, Address a) throws SQLException {
        ProdOrder ps = new ProdOrder();
        ps.setProduct(p);
        ps.setQuantity(rs.getInt("Quantity"));
        ps.setFinalPrice(rs.getFloat("FinalPrice"));
        ps.setAddress(a);
        ps.setStatus(rs.getInt("Status"));
        // to add the reviews to the products use loadProductReview() function
        return ps;
    }
}
