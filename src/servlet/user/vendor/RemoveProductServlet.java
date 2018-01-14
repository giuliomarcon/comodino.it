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
 * Permette al venditore di rimuovere un prodotto.
 */
@WebServlet(name = "RemoveProductServlet", urlPatterns = {"/restricted/vendor/removeproduct"})
public class RemoveProductServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ID = request.getParameter("productID");
        System.out.println("ID PRODOTTO DA RIMUOVERE: " + ID);
        int id = Integer.parseInt(ID);
        HttpSession session = request.getSession(false);
        Shop shop = (Shop) session.getAttribute("shop");
        ShopDao shopDao = new ShopDaoImpl();
        ArrayList<Product> products = shopDao.obtainProducts(shop.getShopID());
        for (Product product : products) {
            if (product.getProductID() == id) {
                product.setQuantity(-1);
                shopDao.editShopProduct(product, shop.getShopID());
            }
        }
        response.sendRedirect("inventory.jsp?success=Prodotto rimosso");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
