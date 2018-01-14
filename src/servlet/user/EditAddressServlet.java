package servlet.user;

import daos.AddressDao;
import daos.impl.AddressDaoImpl;
import main.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Permette di modificare l'indirizzo di spedizione dell'utente loggato.
 */
@WebServlet(name = "EditAddressServlet", urlPatterns = {"/restricted/editaddress"})
public class EditAddressServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String addressID = request.getParameter("AddressID");
        String firstName = request.getParameter("FirstName");
        String lastName = request.getParameter("LastName");
        String address = request.getParameter("Address");
        String city = request.getParameter("City");
        String zip = request.getParameter("Zip");
        String state = request.getParameter("State");
        String phone = request.getParameter("Phone");
        System.out.println("Parametri: " + firstName + " " + lastName + " " + address + " ecc...");
        User user = (User) request.getSession(false).getAttribute("user");
        if (addressID.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || city.isEmpty() || zip.isEmpty() || state.isEmpty() || phone.isEmpty()) {
            System.out.println("[INFO] EditAddress: Missing parameters");
            response.sendRedirect("/restricted/addresses.jsp?error=Parametri mancanti");
            return;
        }
        AddressDao addressDao = new AddressDaoImpl();
        if (addressDao.editAddress(user, addressID, firstName, lastName, address, city, zip, state, phone)) {
            System.out.println("[INFO] EditAddress: Address edited");
            response.sendRedirect("/restricted/addresses.jsp?success=Indirizzo aggiornato");
        } else {
            System.out.println("[INFO] EditAddress: Internal error, address not edited");
            response.sendRedirect("/restricted/addresses.jsp?warning=Indirizzo non aggiornato, riprova...");
        }
    }
}
