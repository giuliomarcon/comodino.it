package servlet.user;

import daos.impl.OrderDaoImpl;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Imposta l'indirizzo di spedizione per un determinato prodotto durante la conferma dell'ordine.
 */
@WebServlet(name = "SetOrderAddressesServlet", urlPatterns = {"/restricted/setorderaddresses"})
public class SetOrderAddressesServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        User user = (User) request.getSession(false).getAttribute("user");
        String address = request.getParameter("address");

        ArrayList<String> ritironegozio = new ArrayList<>();
        if (request.getParameterValues("ritironegozio") != null) {
            List<String> list = Arrays.asList(request.getParameterValues("ritironegozio"));
            ritironegozio.addAll(list);
        }


        if (address == null) {
            response.sendRedirect("checkout.jsp?error=Seleziona un indirizzo valido");
            return;
        }
        boolean result = new OrderDaoImpl().setOrderAddresses(user, address, ritironegozio);
        if (!result) {
            response.sendRedirect("checkout.jsp?error=Errore in fase di inserimento, riprova.");
            return;
        }
        response.sendRedirect("payment.jsp");

    }
}