package servlet.user.vendor;

import daos.ProductDao;
import daos.impl.ProductDaoImpl;
import main.Shop;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Permette l'inserimento di un prodotto totalmente nuovo, ossia non venduto ancora da nessuno.
 */
@MultipartConfig
@WebServlet(name = "InsertNewProductServlet", urlPatterns = {"/restricted/vendor/insertnewproduct"})
public class InsertNewProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            String name = request.getParameter("Name");
            String description = request.getParameter("Description");
            String category = request.getParameter("Category");
            float price = Float.parseFloat(request.getParameter("Price"));
            float discount;
            if (request.getParameter("Discount").isEmpty())
                discount = 0;
            else {
                discount = Float.parseFloat(request.getParameter("Discount").replace(",", "."));
                if (discount < 1)
                    discount = discount;
                else if (discount >= 1 && discount <= 100)
                    discount = discount / 100;
                else
                    discount = 0;
            }
            int quantity = Integer.parseInt(request.getParameter("Quantity"));
            Part productPhoto = request.getPart("productPhoto");
            HttpSession session = request.getSession(false);
            Shop shop = (Shop) session.getAttribute("shop");
            int shopID = shop.getShopID();
            ProductDao productDao = new ProductDaoImpl();
            if (name.isEmpty() || description.isEmpty() || category.isEmpty()) {
                response.sendRedirect("inventory.jsp?error=Tutti i campi sono richiesti");
            } else {
                if (productDao.addNewProduct(shopID, name, description, category, price, discount, quantity, productPhoto))
                    response.sendRedirect("inventory.jsp?success=Prodotto aggiunto all′ inventario");
                else {
                    response.sendRedirect("inventory.jsp?warning=Prodotto già presente in altri negozi. Trova un nome diverso o aggiungi quello");
                }
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("inventory.jsp?error=Errore inserimento dati");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
