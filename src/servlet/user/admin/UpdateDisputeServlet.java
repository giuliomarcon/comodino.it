package servlet.user.admin;

import daos.DisputeDao;
import daos.impl.DisputeDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Aggiorna lo stato della disputa quando l'admin prende una decisione.
 */
@WebServlet(name = "UpdateDisputeServlet", urlPatterns = {"/restricted/admin/updatedispute"})
public class UpdateDisputeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[INFO] UpdateDispute Servlet: Entered");
        if (request.getParameter("orderID") == null || request.getParameter("productID") == null || request.getParameter("shopID") == null || request.getParameter("status") == null) {
            response.sendRedirect("/index.jsp?error=Parametri Mancanti");
            return;
        }
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        int productID = Integer.parseInt(request.getParameter("productID"));
        int shopID = Integer.parseInt(request.getParameter("shopID"));
        int status = Integer.parseInt(request.getParameter("status"));
        System.out.println("\tUpdate Dispute OrderID: " + orderID + " ProductID: " + productID + " ShopID: " + shopID
        );

        DisputeDao disputeDao = new DisputeDaoImpl();
        boolean result = disputeDao.updateDispute(orderID, productID, shopID, status);
        if (!result) { // la modifica della disputa è fallita
            response.sendRedirect("admin_panel.jsp?error=Errore modifica disputa");
            return;
        }
        System.out.println("[INFO] UpdateDispute Servlet: Dispute updated.");
        response.sendRedirect("admin_panel.jsp");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("admin_panel.jsp?error=Percorso invalido");
    }
}
