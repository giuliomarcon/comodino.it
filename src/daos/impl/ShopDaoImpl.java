package daos.impl;

import daos.ShopDao;
import db.DBManager;
import main.*;
import utils.Utils;

import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopDaoImpl implements ShopDao {
    private Connection con;

    public ShopDaoImpl() {
        this.con = DBManager.getCon();
    }

    @Override
    public Shop getShop(int shopID) {
        try {
            // testo se ha il negozio fisico
            PreparedStatement stm = con.prepareStatement("SELECT *\n" +
                    "FROM shop\n" +
                    "INNER JOIN shopinfo USING (ShopID)\n" +
                    "WHERE ShopID = ?");
            stm.setInt(1, shopID);
            ResultSet rs = stm.executeQuery();
            Shop tmp = extractPhysicalShopFromResultSet(rs);

            // se non ha negozio fisico carico solo quello online
            if (tmp == null) {
                PreparedStatement stm2 = con.prepareStatement("SELECT *\n" +
                        "FROM shop\n" +
                        "WHERE ShopID = ?");
                stm2.setInt(1, shopID);
                ResultSet rs2 = stm2.executeQuery();
                tmp = extractSimpleShopFromResultSet(rs2);
            }

            // carico le immagini del negozio
            if (tmp != null) {
                System.out.println("[INFO] Shop preso con successo");
                System.out.flush();
                tmp.setShopphoto(getImages(shopID));
            }
            tmp.setProducts(obtainProducts(shopID));
            tmp.setExpiringProducts(obtainExpiringProducts(shopID));
            return tmp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Product> obtainExpiringProducts(int shopID) {
        ArrayList<Product> expProducts = new ArrayList<>();
        try {
            PreparedStatement stm = con.prepareStatement(
                    "SELECT DISTINCT P.ProductID, P.Name AS ProductName, SP.Quantity \n" +
                            "FROM Product P, ShopProduct SP \n" +
                            "WHERE P.ProductID = SP.ProductID AND SP.ShopID = ? AND Sp.Quantity <= 20 AND Sp.Quantity >= 0\n" +
                            "ORDER BY SP.Quantity \n" +
                            "LIMIT 10"
            );
            stm.setInt(1, shopID);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                p.setQuantity(rs.getInt("Quantity"));

                expProducts.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expProducts;
    }

    public ArrayList<Product> obtainProducts(int shopID) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            PreparedStatement stm = con.prepareStatement(
                    "SELECT DISTINCT P.ProductID, P.Name AS ProductName, SP.Quantity, SP.Price, SP.Discount \n" +
                            "FROM Product P, ShopProduct SP \n" +
                            "WHERE P.ProductID = SP.ProductID AND SP.ShopID = ? AND SP.Quantity >= 0"
            );
            stm.setInt(1, shopID);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                p.setQuantity(rs.getInt("Quantity"));
                p.setPrice(rs.getInt("Price"));
                p.setActualPrice((1 - rs.getFloat("Discount")) * rs.getInt("Price"));
                p.setImgBase64(new ProductDaoImpl().getImages(p.getProductID()));
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public HashMap<String, ProductGroup> getShopProducts(String shopID) {
        HashMap<String, ProductGroup> products = new HashMap<>();
        //Final query PreparedStatement
        try {
            PreparedStatement stm = con.prepareStatement(
                    "SELECT DISTINCT P.ProductID, P.Name AS ProductName, P.CategoryName, P.Rating, SP.ShopID, SP.Price, SP.Discount, SP.Quantity, S.Name AS ShopName,  round(SP.Price * (1-SP.Discount),2) AS ActualPrice \n" +
                            "FROM Product P, ShopProduct SP, Shop S, ShopInfo SI \n" +
                            "WHERE P.ProductID = SP.ProductID AND SP.ShopID = S.ShopID AND S.ShopID = ? AND SP.Quantity > 0 "
            );
            stm.setString(1, shopID);

            //System.out.println("MAIN PRODUCT QUERY: " + stm.toString().substring(45));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

                //Product crafting
                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setShopID(rs.getInt("ShopID"));
                p.setProductName(rs.getString("ProductName"));
                p.setCategoryName(rs.getString("CategoryName"));
                p.setRating(rs.getFloat("Rating"));
                p.setPrice(rs.getFloat("Price"));
                p.setDiscount(rs.getFloat("Discount"));
                p.setActualPrice(rs.getFloat("ActualPrice"));
                p.setQuantity(rs.getInt("Quantity"));
                p.setShopName(rs.getString("ShopName"));

                //ProductGroup crafting
                products.computeIfAbsent(p.getProductName(), k -> new ProductGroup());
                products.get(p.getProductName()).getList().add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //ProductGroup extra fetching
        for (Object o : products.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            ProductGroup gp = (ProductGroup) pair.getValue();
            System.out.println("\nPRODUCT: " + pair.getKey().toString());

            //Decode image from first product
            Product p = gp.getList().get(0);

            try {
                PreparedStatement stm = con.prepareStatement("SELECT * \n" +
                        "FROM productphoto \n" +
                        "WHERE ProductID = ?");
                stm.setInt(1, p.getProductID());
                System.out.println("DECODE PRODUCT IMAGE: " + stm.toString().substring(45));
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    // perché non dovrebbe andare questo, più elegante?
                    gp.setImageData(Utils.getStringfromBlob(rs.getBlob("Image")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return products;
    }

    @Override
    public int hasOtherShops(int productID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT DISTINCT COUNT(*) AS shopcount\n" +
                    "FROM shopproduct sp\n" +
                    "WHERE sp.ProductID = ? AND sp.Quantity > '0'");
            stm.setInt(1, productID);
            ResultSet rs = stm.executeQuery();
            rs.next();
            int res = rs.getInt("shopcount");
            return res;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ArrayList<Shop> getPhysicalShopsByProduct(int productID) {
        ArrayList<Shop> shops = new ArrayList<>();
        try {
            PreparedStatement stm = con.prepareStatement("SELECT s.*, si.*\n" +
                    "FROM shop s\n" +
                    "INNER JOIN shopinfo si ON s.ShopID = si.ShopID\n" +
                    "INNER JOIN shopproduct sp ON s.ShopID = sp.ShopID\n" +
                    "WHERE sp.ProductID = ? AND sp.Quantity > 0");
            stm.setInt(1, productID);

            ResultSet rs = stm.executeQuery();
            shops = extractPhysicalShopsFromResultSet(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shops;
    }

    private ArrayList<Shop> extractSimpleShopsFromResultSet(ResultSet rs) {
        ArrayList<Shop> shopList = new ArrayList<>();

        try {
            Shop shop = new Shop();
            while (rs.next()) {
                shop = new Shop();

                // creo inserisco dati ordine generale
                shop.setShopID(rs.getInt("ShopID"));
                shop.setName(rs.getString("Name"));
                shop.setDescription(rs.getString("Description"));
                shop.setWebsite(rs.getString("Website"));
                shop.setRating(rs.getFloat("Rating"));

                shopList.add(shop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shopList;
    }

    private Shop extractSimpleShopFromResultSet(ResultSet rs) {
        try {
            if (!rs.next()) {
                return null;
            }
            Shop shop = new Shop();

            // creo inserisco dati ordine generale
            shop.setShopID(rs.getInt("ShopID"));
            shop.setName(rs.getString("Name"));
            shop.setDescription(rs.getString("Description"));
            shop.setWebsite(rs.getString("Website"));
            shop.setRating(rs.getFloat("Rating"));
            return shop;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PhysicalShop extractPhysicalShopFromResultSet(ResultSet rs) {
        try {
            if (!rs.next()) {
                return null;
            }
            PhysicalShop shop = new PhysicalShop();

            // creo inserisco dati ordine generale
            shop.setShopID(rs.getInt("ShopID"));
            shop.setName(rs.getString("Name"));
            shop.setDescription(rs.getString("Description"));
            shop.setWebsite(rs.getString("Website"));
            shop.setRating(rs.getFloat("Rating"));

            shop.setLatitude(rs.getFloat("Latitude"));
            shop.setLongitude(rs.getFloat("Longitude"));
            shop.setAddress(rs.getString("Address"));
            shop.setCity(rs.getString("City"));
            shop.setState(rs.getString("State"));
            shop.setZip(rs.getString("ZIP"));
            shop.setOpeninghours(rs.getString("OpeningHours"));
            return shop;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Shop> extractPhysicalShopsFromResultSet(ResultSet rs) {
        ArrayList<Shop> shopList = new ArrayList<>();

        try {
            PhysicalShop physicalShop;

            while (rs.next()) {

                // creo inserisco dati ordine generale
                physicalShop = new PhysicalShop();

                physicalShop.setShopID(rs.getInt("s.ShopID"));
                physicalShop.setName(rs.getString("Name"));
                physicalShop.setDescription(rs.getString("Description"));
                physicalShop.setWebsite(rs.getString("Website"));
                physicalShop.setRating(rs.getFloat("Rating"));

                physicalShop.setLatitude(rs.getFloat("Latitude"));
                physicalShop.setLongitude(rs.getFloat("Longitude"));
                physicalShop.setAddress(rs.getString("Address"));
                physicalShop.setCity(rs.getString("City"));
                physicalShop.setState(rs.getString("State"));
                physicalShop.setZip(rs.getString("ZIP"));
                physicalShop.setOpeninghours(rs.getString("OpeningHours"));

                // aggiungo l'ordine del prodotto al corrispettivo ordine generale
                shopList.add(physicalShop);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shopList;
    }

    public boolean editInfo(Shop shop) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE shop SET Name = ?, Description = ?, Website = ? WHERE ShopID= ?");
            stm.setString(1, shop.getName());
            stm.setString(2, shop.getDescription());
            stm.setString(3, shop.getWebsite());
            stm.setInt(4, shop.getShopID());
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editPhysicalInfo(PhysicalShop shop) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE shop S, shopinfo I SET S.Name = ?, S.Description = ?, S.Website = ?, I.Address = ?, I.City = ?, I.ZIP = ?, I.Latitude = ?, I.Longitude = ?, I.OpeningHours = ? WHERE S.ShopID= ? AND I.ShopID = ?");
            stm.setString(1, shop.getName());
            stm.setString(2, shop.getDescription());
            stm.setString(3, shop.getWebsite());
            stm.setString(4, shop.getAddress());
            stm.setString(5, shop.getCity());
            stm.setString(6, shop.getZip());
            stm.setFloat(7, shop.getLatitude());
            stm.setFloat(8, shop.getLongitude());
            stm.setString(9, shop.getOpeninghours());
            stm.setInt(10, shop.getShopID());
            stm.setInt(11, shop.getShopID());
            stm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int createNewShop(User user, String shopName, String shopDescription, String shopWebsite) {
        if (user.hasShop()) {
            return 0;
        }
        int shopID = 0;
        try {
            PreparedStatement stm = con.prepareStatement(
                    "INSERT INTO shop (Rating, Name, Description, Website) VALUES (-1,?,?,?)", Statement.RETURN_GENERATED_KEYS
            );
            stm.setString(1, shopName);
            stm.setString(2, shopDescription);
            stm.setString(3, shopWebsite);
            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            rs.next();

            shopID = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (shopID != 0) {
            try {
                PreparedStatement stm = con.prepareStatement("INSERT INTO usershop (UserID, ShopID) VALUES (?,?)");
                stm.setInt(1, user.getUserID());
                stm.setInt(2, shopID);
                stm.executeUpdate();
                user.setShopID(shopID);
                return shopID;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return 0;
    }

    @Override
    public boolean createNewPhysicalShop(User user, String shopName, String shopDescription, String shopWebsite, String shopAddress, String shopCity, String shopState, String shopZIP, String shopOpeningHours, Float shopLatitude, Float shopLongitude) {
        int shopID = createNewShop(user, shopName, shopDescription, shopWebsite);
        if (shopID != 0) {
            try {
                PreparedStatement stm = con.prepareStatement("INSERT INTO shopinfo (ShopID, Latitude, Longitude, Address, City, State, ZIP, OpeningHours) VALUES (?,?,?,?,?,?,?,?)");
                stm.setInt(1, shopID);
                stm.setFloat(2, shopLatitude);
                stm.setFloat(3, shopLongitude);
                stm.setString(4, shopAddress);
                stm.setString(5, shopCity);
                stm.setString(6, shopState);
                stm.setString(7, shopZIP);
                stm.setString(8, shopOpeningHours);
                stm.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public boolean editShopProduct(Product product, int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE shopproduct SET Price=?, Discount=?, Quantity=? WHERE ShopID = ? AND ProductID = ?");
            stm.setFloat(1, product.getPrice());
            stm.setFloat(2, product.getDiscount());
            stm.setInt(3, product.getQuantity());
            stm.setInt(4, shopID);
            stm.setInt(5, product.getProductID());
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean addPhysicalShop(int shopID, PhysicalShop shop) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO shopinfo VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stm.setFloat(1, shop.getLatitude());
            stm.setFloat(2, shop.getLongitude());
            stm.setString(3, shop.getAddress());
            stm.setString(4, shop.getCity());
            stm.setString(5, shop.getState());
            stm.setString(6, shop.getZip());
            stm.setString(7, shop.getOpeninghours());
            stm.setInt(8, shopID);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addShopPhoto(int shopID, Part shopPhoto) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO shopphoto VALUES (NULL, ?, ?)");
            stm.setBinaryStream(1, shopPhoto.getInputStream(), (int) shopPhoto.getSize());
            stm.setInt(2, shopID);
            stm.executeUpdate();
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public ArrayList<String> getImages(int shopID) {

        ArrayList<String> imgBase64 = new ArrayList<>();

        try {
            PreparedStatement stm = con.prepareStatement("SELECT *\n" +
                    "FROM shopphoto sp\n" +
                    "WHERE sp.ShopID = ?");
            stm.setInt(1, shopID);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                imgBase64.add(Utils.getStringfromBlob(rs.getBlob("Image")));
            }
            if (imgBase64.size() == 0) {
                imgBase64.add("/placeholdershop.jpg");
            }
        } catch (SQLException e) {
            imgBase64.add("/ImageNotFound.png");
            e.printStackTrace();
        }
        return imgBase64;
    }

    public ArrayList<Shop> allShops() {
        ArrayList<Shop> shops = new ArrayList<>();
        try {
            PreparedStatement stm = con.prepareStatement("SELECT *\n" +
                    "FROM shop\n" +
                    "LEFT OUTER JOIN shopinfo s ON shop.ShopID = s.ShopID;");
            ResultSet rs = stm.executeQuery();
            shops = extractSimpleShopsFromResultSet(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shops;
    }

    public boolean updateShopRating(int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE shop S SET Rating = (SELECT AVG(Rating) FROM shopreview sp WHERE sp.ShopID = ?)");
            stm.setInt(1,shopID);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateLatLong(int shopID, float latitude, float longitude) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE shopinfo SET Latitude = ?, Longitude = ? WHERE ShopID = ?");
            stm.setFloat(1, latitude);
            stm.setFloat(2, longitude);
            stm.setInt(3, shopID);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int countBadReports(int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT *\n" +
                    "FROM shop s, dispute d " +
                    "WHERE s.ShopID = ? AND s.ShopID = d.ShopID AND d.Status = 1");
            stm.setInt(1, shopID);
            System.out.println(stm.toString());
            ResultSet rs = stm.executeQuery();
            if (rs != null) {
                rs.beforeFirst();
                rs.last();
                return rs.getRow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
