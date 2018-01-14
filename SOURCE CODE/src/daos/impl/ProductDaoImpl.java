package daos.impl;

import daos.ProductDao;
import db.DBManager;
import info.debatty.java.stringsimilarity.JaroWinkler;
import main.Product;
import main.ProductGroup;
import main.Shop;
import utils.Utils;

import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static utils.Mechanist.checkSMP;
import static utils.Mechanist.getMMP;

public class ProductDaoImpl implements ProductDao {
    private final static String sortKeyword = "sort";
    private final static String minPriceKeyword = "minPrice";
    private final static String maxPriceKeyword = "maxPrice";
    private final static String minRatingKeyword = "minRat";
    private final static String categoryKeyword = "cat";
    private final static String vendorKeyword = "vendor";
    private final static String geolocalizationKeyword = "geo";
    private Connection con;

    public ProductDaoImpl() {
        this.con = DBManager.getCon();
    }

    @Override
    public Product getProduct(int productID, int shopID) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT *, s.Name AS ShopName \n" +
                    "FROM product AS p\n" +
                    "INNER JOIN shopproduct AS sp USING (ProductID)\n" +
                    "INNER JOIN shop AS s USING (ShopID)\n" +
                    "WHERE p.ProductID = ? AND sp.ShopID = ?;");
            stm.setInt(1, productID);
            stm.setInt(2, shopID);
            ResultSet rs = stm.executeQuery();
            return extractProductFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Product> allProducts() {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT *\n" +
                    "FROM product AS p\n" +
                    "INNER JOIN shopproduct AS sp USING (ProductID)\n" +
                    "GROUP BY ProductID;");
            ResultSet rs = stm.executeQuery();
            return extractAllProductsFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Product> extractAllProductsFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        Product prod;
        while (true) {
            prod = extractProductFromResultSet(rs);
            if (prod == null)
                break;
            products.add(prod);
        }
        return products;
    }

    private Product extractProductFromResultSet(ResultSet rs) {
        try {
            if (!rs.next()) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Product prod = new Product();

        try {
            prod.setProductID(rs.getInt("ProductID"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setProductName(rs.getString("Name"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setShopID(rs.getInt("ShopID"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setShopName(rs.getString("ShopName"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setDescription(rs.getString("Description"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setImgBase64(getImages(prod.getProductID()));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setPrice(rs.getFloat("Price"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setQuantity(rs.getInt("Quantity"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setDiscount(rs.getFloat("Discount"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setActualPrice(prod.getPrice() * (1 - prod.getDiscount()));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setCategoryName(rs.getString("CategoryName"));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            prod.setRating(rs.getFloat("Rating"));
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return prod;
    }

    @Override
    public List<Map.Entry<String, ProductGroup>> getProducts(Map params) {
        Map<String, ProductGroup> products = new HashMap<>();

        //SearchServlet query parameter
        String searchQuery;
        if ((searchQuery = checkSMP(params.get("q"))) == null) {
            return null;
        }

        //Sorting parameter
        String sort = checkSMP(params.get(sortKeyword)),
                orderBySql = "ORDER BY ActualPrice ASC, P.Rating DESC, S.Rating DESC, SP.Quantity DESC";
        if (sort != null) {
            switch (sort) {
                case "price-asc":
                    orderBySql = " ORDER BY ActualPrice ASC, P.Rating DESC, S.Rating DESC, SP.Quantity DESC";
                    break;
                case "price-desc":
                    orderBySql = " ORDER BY ActualPrice DESC, P.Rating DESC, S.Rating DESC, SP.Quantity DESC";
                    break;
                case "rating-desc":
                    orderBySql = " ORDER BY P.Rating DESC, ActualPrice ASC, S.Rating DESC, SP.Quantity DESC";
                    break;
                default:
                    break;
            }
        }

        //minPrice parameter
        String minPrice, maxPrice;
        if ((minPrice = checkSMP(params.get(minPriceKeyword))) != null) {
            try {
                int value = Integer.parseInt(minPrice);
                if (value >= 0) {
                    minPrice = " ActualPrice >= " + minPrice + " ";
                } else minPrice = "";
            } catch (NumberFormatException e) {
                e.printStackTrace();
                minPrice = "";
            }
        } else minPrice = "";
        //maxPrice parameter
        if ((maxPrice = checkSMP(params.get(maxPriceKeyword))) != null) {
            try {
                int value = Integer.parseInt(maxPrice);
                if (value > 0) {
                    maxPrice = " ActualPrice <= " + maxPrice + " ";
                } else maxPrice = "";
            } catch (NumberFormatException e) {
                e.printStackTrace();
                maxPrice = "";
            }
        } else maxPrice = "";
        //price range;
        String priceRange = " HAVING ";
        if (minPrice.equals("")) {
            if (maxPrice.equals("")) {
                priceRange = "";
            } else {
                priceRange += maxPrice;
            }
        } else {
            if (maxPrice.equals("")) {
                priceRange += minPrice;
            } else {
                priceRange += minPrice + " AND " + maxPrice;
            }
        }

        //minRating parameter
        String minRating;
        if ((minRating = checkSMP(params.get(minRatingKeyword))) != null) {
            try {
                int value = Integer.parseInt(minRating);
                if (!(value >= 1 && value <= 5)) {
                    minRating = "";
                } else {
                    minRating = " AND P.Rating >= " + minRating + " ";
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                minRating = "";
            }
        } else minRating = "";

        //Category parameter
        String[] cat;
        StringBuilder category = new StringBuilder();
        if ((cat = getMMP(params.get(categoryKeyword))) != null) {
            for (int i = 0; i < cat.length; i++) {
                //System.out.println(cat[i]);
                if (i == 0)
                    category.append(" AND (P.CategoryName = '")
                            .append(cat[i])
                            .append("'")
                            .append((i == cat.length - 1) ? ") " : "");
                else
                    category.append(" OR P.CategoryName = '")
                            .append(cat[i])
                            .append("'")
                            .append((i == cat.length - 1) ? ") " : "");
            }
        }

        //Vendor parameter
        String[] ven;
        StringBuilder vendor = new StringBuilder();
        if ((ven = getMMP(params.get(vendorKeyword))) != null) {
            for (String aVen : ven) {
                vendor.append(" AND (S.Name <> '").append(aVen).append("')");
            }
        }

        //Regione amministrativa
        String[] geo;
        StringBuilder region = new StringBuilder();
        if ((geo = getMMP(params.get(geolocalizationKeyword))) != null) {
            region.append(" AND S.ShopID = SI.ShopID ");
            for (String aGeo : geo)
                region.append(" AND (SI.State <> '").append(aGeo).append("')");
        }

        //Final query PreparedStatement
        //System.out.println("MAIN PRODUCT QUERY: " + stm.toString().substring(45));

        //Final query execute
        try (PreparedStatement stm = con.prepareStatement(
                "SELECT DISTINCT P.ProductID, P.Name as ProductName, P.CategoryName, P.Rating, " +
                        "SP.ShopID, SP.Price, SP.Discount, SP.Quantity, S.Name as ShopName, " +
                        " round(SP.Price * (1-SP.Discount),2) as ActualPrice " +
                        "FROM Product P, ShopProduct SP, Shop S, ShopInfo SI " +
                        "WHERE P.ProductID = SP.ProductID AND SP.ShopID = S.ShopID AND SP.Quantity > 0 " +
                        region.toString() + category.toString() + vendor.toString() + minRating +
                        priceRange +
                        orderBySql
        )) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    //JaroWinkler implementation
                    JaroWinkler jw = new JaroWinkler();
                    double distance = jw.similarity(rs.getString("ProductName").toLowerCase(), searchQuery.toLowerCase());
                    if (searchQuery.equals("") || distance >= 0.7) {
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

                        products.get(p.getProductName()).setLastDistance(distance);

                        /*if(distance > products.get(p.getProductName()).getLastDistance()){
                            products.get(p.getProductName()).setLastDistance(distance);
                        }*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //ProductGroup extra fetching
        for (Map.Entry pair : products.entrySet()) {
            ProductGroup gp = (ProductGroup) pair.getValue();
            //System.out.println("\nPRODUCT: " + pair.getKey().toString());

            //Review count info

            try (PreparedStatement stm = con.prepareStatement(
                    "SELECT COUNT(*) AS conto\n" +
                            "FROM productreview\n" +
                            "WHERE ProductID = ?")) {
                stm.setInt(1, gp.getList().get(0).getProductID());
                try (ResultSet rs = stm.executeQuery()) {
                    rs.next();
                    //System.out.println("REVIEW COUNT: " + rs.getInt("conto"));
                    gp.setReviewCount(rs.getInt("conto"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Decode image from first product
            Product p = gp.getList().get(0);
            try (PreparedStatement stm = con.prepareStatement("SELECT * FROM productphoto WHERE ProductID = ?")) {
                stm.setInt(1, p.getProductID());
                //System.out.println("DECODE PRODUCT IMAGE: " + stm.toString().substring(45));
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        gp.setImageData(Utils.getStringfromBlob(rs.getBlob("Image")));
                    } else {
                        gp.setImageData("/ImageNotFound.png");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //Fetch shop list related to this ProductGroup
            try (PreparedStatement stm = con.prepareStatement("SELECT shop.*, shopproduct.Price, shopproduct.Discount, " +
                    "shopproduct.Quantity, round(shopproduct.Price * (1-shopproduct.Discount),2) AS ActualPrice " +
                    "FROM product, shopproduct, shop " +
                    "WHERE product.name = ? AND shopproduct.Quantity > 0 " +
                    "AND product.ProductID = shopproduct.ProductID AND shopproduct.ShopID = shop.ShopID " +
                    "ORDER BY ActualPrice ASC, Rating DESC")) {
                stm.setString(1, pair.getKey().toString());
                //System.out.println("FETCH SHOP LIST: " + stm.toString().substring(45));
                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        //Shop crafting
                        Shop s = new Shop();
                        s.setShopID(rs.getInt("ShopID"));
                        s.setName(rs.getString("Name"));
                        s.setDescription(rs.getString("Description"));
                        s.setWebsite(rs.getString("Website"));
                        s.setRating(rs.getFloat("Rating"));
                        s.setSampleActualPrice(rs.getFloat("ActualPrice"));

                        //Actual insertion
                        gp.getVendors().add(s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Fetch geozone list related to this ProductGroup
            try (PreparedStatement stm = con.prepareStatement(
                    "SELECT DISTINCT SI.State " +
                            "FROM Product P, ShopProduct SP, Shop S, ShopInfo SI " +
                            "WHERE P.Name = ? AND P.ProductID = SP.ProductID AND SP.ShopID = S.ShopID AND SP.Quantity > 0 "
            )) {
                stm.setString(1, pair.getKey().toString());

                //Execution
                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        gp.getGeo().add(rs.getString("State"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Set<Map.Entry<String, ProductGroup>> set = products.entrySet();
        List<Map.Entry<String, ProductGroup>> list = new ArrayList<>(set);
        list.sort(Comparator.comparingDouble((Map.Entry<String, ProductGroup> o) -> o.getValue().getLastDistance()).reversed());

        for (Map.Entry<String, ProductGroup> entry : list) {
            System.out.println(entry.getKey() + " ==== " + entry.getValue().getLastDistance());
        }

        return list;
    }

    @Override
    public boolean checkAvailability(int productID, int shopID, int quantity) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT *\n" +
                    "FROM shopproduct\n" +
                    "WHERE ProductID = ? AND ShopID = ?");
            stm.setInt(1, productID);
            stm.setInt(2, shopID);
            ResultSet rs = stm.executeQuery();
            Product p = extractProductFromResultSet(rs);
            return p.getQuantity() >= quantity;
        } catch (SQLException e) {
            System.out.println("[ERROR] Non esiste il prodotto in shop product (pid:"+productID+", sid:"+shopID+", qty:"+quantity+")");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean reduceAvailability(int productID, int shopID, Integer quantity) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE shopproduct SET Quantity = Quantity - ?\n" +
                    "WHERE ProductID = ? AND ShopID = ? AND Quantity >= ?");
            stm.setInt(1, quantity);
            stm.setInt(2, productID);
            stm.setInt(3, shopID);
            stm.setInt(4, quantity);
            stm.executeUpdate();
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateProductRating(int productID) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE product P \n" +
                    "SET Rating = (SELECT avg(Rating) AS ratingreview \n" +
                    "              FROM productreview \n" +
                    "              WHERE ProductID = P.ProductID) \n" +
                    "WHERE ProductID = ?");
            stm.setInt(1, productID);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getImages(int productID) {

        ArrayList<String> imgBase64 = new ArrayList<>();

        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM productphoto WHERE ProductID = ?");
            stm.setInt(1, productID);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                imgBase64.add(Utils.getStringfromBlob(rs.getBlob("Image")));
            }
            if (imgBase64.size() == 0) {
                imgBase64.add("/ImageNotFound.png");
            }
        } catch (SQLException e) {
            imgBase64.add("/ImageNotFound.png");
            e.printStackTrace();
        }
        return imgBase64;
    }

    @Override
    public String getAutocompleteProducts(String term) {
        StringBuilder products = new StringBuilder("[");
        try {
            PreparedStatement stm = con.prepareStatement("SELECT Name FROM product");
            JaroWinkler jw = new JaroWinkler();
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                double distance = jw.similarity(rs.getString("Name").toLowerCase(), term.toLowerCase());
                if (term.equals("") || distance >= 0.7) {
                    products.append("\"").append(rs.getString("Name")).append("\",");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String list = removeLastChar(products.toString());
        list += "]";
        //.out.println(list);
        return list;
    }

    private String removeLastChar(String str) {
        if (str.equals("["))
            return str;
        return str.substring(0, str.length() - 1);
    }

    public int checkProductStatus(int shopID, String productName) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT P.Name, SP.ShopID, P.ProductID FROM product P, shopproduct SP WHERE P.ProductID = SP.ProductID AND P.Name LIKE ? AND SP.ShopID = ?");
            stm.setString(1, productName);
            stm.setInt(2, shopID);
            ResultSet rs = stm.executeQuery();
            if (!rs.isBeforeFirst()) {
                return 0;
            } else {
                rs.next();
                return rs.getInt("ProductID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean restoreProduct(int shopID, int productID) {
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE shopproduct SP SET Quantity = 0 WHERE SP.ProductID = ? AND SP.ShopID = ?");
            stm.setInt(1, productID);
            stm.setInt(2, shopID);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getSimilarProducts(ArrayList<Product> products, String productName) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT P.Name, P.ProductID FROM product P");
            JaroWinkler jw = new JaroWinkler();
            ResultSet rs = stm.executeQuery();
            Product tmp;
            while (rs.next()) {
                double distance = jw.similarity(rs.getString("Name").toLowerCase(), productName.toLowerCase());
                if (distance >= 0.7) {
                    tmp = new Product();
                    tmp.setProductName(rs.getString("Name"));
                    tmp.setProductID(rs.getInt("ProductID"));
                    tmp.setImgBase64(getImages(rs.getInt("ProductID")));
                    System.out.println("AGGIUNGO: " + tmp.getProductName().toUpperCase());
                    products.add(tmp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addShopProduct(int shopID, int productID, int quantity, float price, float discount) {
        try {
            PreparedStatement rst = con.prepareStatement("SELECT * FROM shopproduct WHERE ProductID = ? AND ShopID = ?");
            rst.setInt(1, productID);
            rst.setInt(2, shopID);
            ResultSet rs = rst.executeQuery();
            PreparedStatement stm;
            if (!rs.isBeforeFirst()) {
                stm = con.prepareStatement("INSERT INTO shopproduct VALUES (?,?,?,?,?)");
                stm.setFloat(1, price);
                stm.setInt(2, quantity);
                stm.setFloat(3, discount);
                stm.setInt(4, productID);
                stm.setInt(5, shopID);
                stm.executeUpdate();
                return true;
            } else
                stm = con.prepareStatement("UPDATE shopproduct SET Price = ?, Discount = ?, Quantity = ? WHERE ShopID = ? AND ProductID = ?");
            stm.setFloat(1, price);
            stm.setFloat(2, discount);
            stm.setInt(3, quantity);
            stm.setInt(4, shopID);
            stm.setInt(5, productID);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addNewProduct(int shopID, String name, String description, String category, float price, float discount, int quantity, Part productPhoto) {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT ProductID FROM product WHERE Name = ?");
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            if (rs.isBeforeFirst()) {
                return false;
            } else {
                PreparedStatement stm1 = con.prepareStatement("INSERT INTO product VALUES (NULL, ?, ?, -1, ?)");
                stm1.setString(1, name);
                stm1.setString(2, description);
                stm1.setString(3, category);
                stm1.executeUpdate();
                PreparedStatement stm2 = con.prepareStatement("SELECT productID FROM product WHERE Name = ?");
                stm2.setString(1, name);
                rs = stm2.executeQuery();
                rs.next();
                int productID = rs.getInt("productID");
                PreparedStatement stm3 = con.prepareStatement("INSERT INTO shopproduct VALUES (?,?,?,?,?)");
                stm3.setFloat(1, price);
                stm3.setInt(2, quantity);
                stm3.setFloat(3, discount);
                stm3.setInt(4, productID);
                stm3.setInt(5, shopID);
                stm3.executeUpdate();
                PreparedStatement stm4 = con.prepareStatement("INSERT INTO productphoto VALUES (NULL ,?,?)");
                stm4.setBinaryStream(1, productPhoto.getInputStream(), (int) productPhoto.getSize());
                stm4.setInt(2, productID);
                stm4.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addProductPhoto(int productID, Part productPhoto) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO productphoto VALUES (NULL,?,?)");
            stm.setBinaryStream(1, productPhoto.getInputStream(), (int) productPhoto.getSize());
            stm.setInt(2, productID);
            stm.executeUpdate();
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setAutoCommit(boolean b) {
        try {
            con.setAutoCommit(b);
        } catch (Exception ignored) {
        }
    }

    public void rollback() {
        try {
            con.rollback();
        } catch (Exception ignored) {
        }
    }
}
