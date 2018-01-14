package servlet;

import db.DBManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Recupera i venditori che vendono un determinato prodotto e si occupa di stamparli
 */
@WebServlet(name = "GetVendorServlet", urlPatterns = {"/getVendorServlet"})
public class GetVendorsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = DBManager.getCon();

        if (request.getParameter("getQuantity").equals("1")) {
            PreparedStatement stm;
            try {
                stm = con.prepareStatement("SELECT COUNT(*) AS Conto FROM product, shopproduct, shop WHERE product.name = ? AND shopproduct.Quantity > 0 AND product.ProductID = shopproduct.ProductID AND shopproduct.ShopID = shop.ShopID ");

                String ret = "";
                stm.setString(1, request.getParameter("nome_prodotto"));
                try (ResultSet rs = stm.executeQuery()) {
                    System.out.println(stm.toString());
                    while (rs.next()) {
                        ret += rs.getInt("Conto");
                    }

                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(ret);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stm.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            if (request.getParameter("nome_prodotto") != null) {
                PreparedStatement stm;
                try {
                    stm = con.prepareStatement("SELECT product.ProductID, shop.*, shopproduct.Price, shopproduct.Discount, shopproduct.Quantity, round(shopproduct.Price * (1-shopproduct.Discount),2) as ActualPrice \n" +
                            "FROM product, shopproduct, shop \n" +
                            "WHERE product.name = ? AND shopproduct.Quantity > 0 AND product.ProductID = shopproduct.ProductID AND shopproduct.ShopID = shop.ShopID \n" +
                            "ORDER BY ActualPrice ASC, Rating DESC\n" +
                            "LIMIT " + request.getParameter("quanti") + " OFFSET " + request.getParameter("offset")
                    );

                    String ret = "";
                    stm.setString(1, request.getParameter("nome_prodotto"));
                    try (ResultSet rs = stm.executeQuery()) {
                        System.out.println(stm.toString());
                        while (rs.next()) {
                            ret += "<div class=\"col-md-8 col-xs-8 mod\"><a href=\"/product.jsp?product=" + rs.getInt("ProductID") + "&shop=" + rs.getInt("ShopID") + "\">" + rs.getString("Name") + "</a></div>\n" +
                                    "                                    <div class=\"col-md-4 col-xs-4 mod text-left\">\n" +
                                    "                                        <span class=\"white valign\">da " + rs.getFloat("ActualPrice") + " €</span>\n" +
                                    "                                        <span class=\"float-right\"><a href=\"/product.jsp?product=" + rs.getString("ProductID") + "&shop=" + rs.getString("ShopID") + "\"><i class=\"fa fa-angle-double-right white valign\" aria-hidden=\"true\"></i></a></span>\n" +
                                    "                                    </div>";
                        }

                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(ret);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        stm.close();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
