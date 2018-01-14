package servlet.user.vendor;

import daos.ProductDao;
import daos.impl.ProductDaoImpl;
import main.Shop;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Se un prodotto è già venduto da altri negozi questo servlet si occupa
 * di includerlo in un nuovo negozio.
 */
@WebServlet(name = "InsertIntoShopProductServlet", urlPatterns = {"/restricted/vendor/insertintoshopproduct"})
public class InsertIntoShopProductServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            Shop shop = (Shop) session.getAttribute("shop");
            int shopID = shop.getShopID();
            int productID = Integer.parseInt(request.getParameter("productID"));
            int quantity = Integer.parseInt(request.getParameter("Quantity"));
            float price = Integer.parseInt(request.getParameter("Price"));
            float discount;
            if (!request.getParameter("Discount").isEmpty())
                discount = Float.parseFloat(request.getParameter("Discount"));
            else
                discount = 0;
            ProductDao productDao = new ProductDaoImpl();
            if (productDao.addShopProduct(shopID, productID, quantity, price, discount))
                response.sendRedirect("inventory.jsp?success=Prodotto aggiunto con successo");
            else
                response.sendRedirect("inventory.jsp?error=Prodotto non aggiunto");
        } catch (NumberFormatException e) {
            response.sendRedirect("inventory.jsp?error=Errore inserimento dati");
        }

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
