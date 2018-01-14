package servlet.user.vendor;

import daos.ShopDao;
import daos.impl.ShopDaoImpl;
import main.Product;
import main.Shop;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Permette di modificare il prezzo di un prodotto in vendita aggiungendo un eventuale sconto.
 */
@WebServlet(name = "EditProductPriceServlet", urlPatterns = {"/restricted/vendor/editproductprice"})
public class EditProductPriceServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String price = request.getParameter("Price");
            String discount = request.getParameter("Discount");
            int id = Integer.parseInt(request.getParameter("productID"));
            HttpSession session = request.getSession(false);
            Shop shop = (Shop) session.getAttribute("shop");
            if (!price.isEmpty() || !discount.isEmpty()) {
                float pr;
                float ds;
                ShopDao shopDao = new ShopDaoImpl();
                ArrayList<Product> products = shopDao.obtainProducts(shop.getShopID());
                for (Product product : products) {
                    if (product.getProductID() == id) {
                        if (!price.isEmpty()) {
                            pr = Float.parseFloat(price);
                            product.setPrice(pr);
                        }
                        if (!discount.isEmpty()) {
                            ds = Float.parseFloat(discount.replace(",", "."));
                            if (ds < 1)
                                product.setDiscount(ds);
                            else if (ds >= 1 && ds <= 100)
                                product.setDiscount(ds / 100);
                            else
                                product.setDiscount(0);
                        }
                        shopDao.editShopProduct(product, shop.getShopID());
                    }
                }

                response.sendRedirect("inventory.jsp?success=Prezzo aggiornato");
            } else
                response.sendRedirect("inventory.jsp?success=Nessuna modifica effettuata");
        } catch (NumberFormatException e) {
            response.sendRedirect("inventory.jsp?error=Errore inserimento dati");
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
