package servlet.autentication;

import daos.UserDao;
import daos.impl.UserDaoImpl;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Gestisce l'invio di email di reset password.
 */
@WebServlet(name = "PasswordRequestServlet", urlPatterns = {"/passwordRequest"})
public class PasswordRequestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (Utils.isNullOrEmpty(request.getParameter("email"))) {
            response.sendRedirect("/index.jsp");
            return;
        }

        String email = request.getParameter("email");

        UserDao user = new UserDaoImpl();
        if (user.checkEmail(email)) {
            String passwordResetToken = Utils.sendResetEmail(email);
            if (passwordResetToken != null) {
                if (user.updateResetToken(email, passwordResetToken)) {
                    response.sendRedirect("/index.jsp?success=Email di recupero password inviata con successo");
                } else {
                    response.sendRedirect("/index.jsp?error=Errore: impossibile generare il token di recupero");
                }
            } else {
                response.sendRedirect("/index.jsp?error=Errore: connessione SMPT fallita");
            }
        } else {
            System.out.println("[SECURITY WARNING] Tentativo di recupero password per una mail non associata a nessun account " +
                    "IP: " + request.getRemoteAddr());
            response.sendRedirect("/index.jsp?success=Email di recupero password inviata con successo");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
