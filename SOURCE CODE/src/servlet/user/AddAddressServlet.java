package servlet.user;

import daos.impl.AddressDaoImpl;
import main.User;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Gestisce l'aggiunta di un nuovo indirizzo di spedizione per l'utente
 */
@WebServlet(name = "AddAddressServlet", urlPatterns = {"/restricted/addaddress"})
public class AddAddressServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession(false).getAttribute("user");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String zip = request.getParameter("zip");
        String state = request.getParameter("state");
        String phonenumber = request.getParameter("phonenumber");
        if (firstname == null || lastname == null || address == null || city == null || zip == null || state == null || phonenumber == null) {
            response.sendRedirect("addresses.jsp?error=Completa tutti i campi");
        }
        boolean result = new AddressDaoImpl().addAddress(user.getUserID(), firstname, lastname, address, city, zip, state, phonenumber);
        if (!result) {
            response.sendRedirect("addresses.jsp?error=Errore in fase di inserimento, riprova.");
            return;
        }
        if (!Utils.isNullOrEmpty(request.getParameter("from"))) {
            response.sendRedirect("addresses.jsp?success=Indirizzo aggiunto!&from=" + request.getParameter("from"));
            return;
        }
        response.sendRedirect("addresses.jsp?success=Indirizzo aggiunto!");

    }
}