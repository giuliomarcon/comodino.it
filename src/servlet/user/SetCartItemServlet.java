package servlet.user;

import daos.impl.UserDaoImpl;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Modifica una quantità di un prodotto presente nel carrello.
 */
@WebServlet(name = "setCartItem", urlPatterns = {"/restricted/setcartitem"})
public class SetCartItemServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        int productID = Integer.parseInt(request.getParameter("productID"));
        int shopID = Integer.parseInt(request.getParameter("shopID"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        if (user != null) {
            new UserDaoImpl().setCartItem(user, productID, shopID, quantity);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}