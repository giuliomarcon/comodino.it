package servlet.user;

import daos.impl.AddressDaoImpl;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Rimuove un indirizzo di spedizione dal database.
 */
@WebServlet(name = "RemoveAddressServlet", urlPatterns = {"/restricted/removeaddress"})
public class RemoveAddressServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        int addressID = Integer.parseInt(request.getParameter("addressID"));
        if (request.getParameter("addressID") == null) {
            return;
        }
        boolean result = new AddressDaoImpl().removeAddress(addressID, user.getUserID());
        System.out.println("[INFO] RemoveAddress: " + (result ? "Indirizzo rimosso" : "Errore rimozione indirizzo"));
    }
}