package servlet.user;

import daos.DisputeDao;
import daos.NotificationDao;
import daos.impl.DisputeDaoImpl;
import daos.impl.NotificationDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Dato un prodotto acquistato permette all'utente di aprire una disputa.
 */
@WebServlet(name = "OpenDisputeServlet", urlPatterns = {"/restricted/opendispute"})
public class OpenDisputeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[INFO] OpenDispute Servlet: Entered");
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("title") == null || request.getParameter("description") == null || request.getParameter("orderID") == null || request.getParameter("productID") == null || request.getParameter("shopID") == null) {
            response.sendRedirect("/index.jsp?error=Parametri Mancanti");
            return;
        }
        String title = request.getParameter("title");
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        int productID = Integer.parseInt(request.getParameter("productID"));
        int shopID = Integer.parseInt(request.getParameter("shopID"));
        String description = request.getParameter("description");
        System.out.println("\tDispute OrderID: " + orderID + " ProductID: " + productID + " ShopID: " + shopID +
                "\n\tTitle: " + title +
                "\n\tDescription: " + description
        );

        DisputeDao disputeDao = new DisputeDaoImpl();
        boolean result = disputeDao.createDispute(orderID, productID, shopID, title, description);
        if (result) {
            NotificationDao notificationDao = new NotificationDaoImpl();
            result = notificationDao.createDisputeNotification(title, orderID, productID, shopID);
        } else { // la creazione della disputa è fallita
            response.sendRedirect("/index.jsp?error=Errore Creazione Disputa");
            return;
        }
        if (!result) { // la creazione della notifica della disputa è fallita
            response.sendRedirect("/index.jsp?error=Errore Creazione Notifica Disputa");
            return;
        }
        System.out.println("[INFO] OpenDispute Servlet: Dispute created.");
        response.sendRedirect("orderhistory.jsp");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp?error=Percorso invalido");
    }
}
