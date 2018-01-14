package servlet.cart;

import daos.impl.UserDaoImpl;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Riduce di 1 la quantità di un determinato prodotto dal carrello.
 */
@WebServlet(name = "DecreaseCartItemServlet", urlPatterns = {"/decreasecartitem"})
public class DecreaseCartItemServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        int productID = Integer.parseInt(request.getParameter("productID"));
        int shopID = Integer.parseInt(request.getParameter("shopID"));
        new UserDaoImpl().decreaseCartItem(user, productID, shopID);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}