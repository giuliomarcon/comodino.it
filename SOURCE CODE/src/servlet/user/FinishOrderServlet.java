package servlet.user;

import daos.OrderDao;
import daos.impl.OrderDaoImpl;
import main.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Si occupa di gestire la conferma di avvenuta ricezione dell'ordine.
 */
@WebServlet(name = "FinishOrderServlet", urlPatterns = {"/restricted/finishorder"})
public class FinishOrderServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderID = req.getParameter("order");
        String productID = req.getParameter("product");
        String shopID = req.getParameter("shop");
        User user = (User) req.getSession(false).getAttribute("user");
        if (orderID.isEmpty() || productID.isEmpty() || shopID.isEmpty()) {
            System.out.println("[INFO] FinishOrder: Missing parameters");
            resp.sendRedirect("/restricted/orderhistory.jsp?error=Parametri mancanti");
            return;
        }
        OrderDao orderDao = new OrderDaoImpl();
        if (orderDao.finishOrderProd(user.getUserID(), orderID, productID, shopID)) {
            System.out.println("[INFO] FinishOrder: Product marked finished");
            resp.sendRedirect("/restricted/orderhistory.jsp");
        } else {
            System.out.println("[INFO] FinishOrder: Internal error, product not finished");
            resp.sendRedirect("/restricted/orderhistory.jsp?warning=Prodotto non trovato, riprova...");
        }
    }
}
