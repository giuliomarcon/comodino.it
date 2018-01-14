package servlet.autentication;

import daos.impl.UserDaoImpl;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Si occupa di inviare un email di conferma all'indirizzo email fornito in fase di registrazione.
 */
@WebServlet(name = "EmailConfirmServlet", urlPatterns = {"/emailConfirm"})
public class EmailConfirmServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token;

        //Quick check
        if (Utils.isNullOrEmpty(token = request.getParameter("token"))) {
            return;
        }

        boolean confirmed = new UserDaoImpl().confirm(request.getParameter("token"));

        if (confirmed) {
            response.sendRedirect("/index.jsp?success=Email verificata con successo, ora puoi effettuare il login");
        } else {
            response.sendRedirect("/index.jsp");
        }
    }
}
