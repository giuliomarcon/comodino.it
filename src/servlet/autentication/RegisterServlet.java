package servlet.autentication;

import daos.impl.UserDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Registra un nuovo utente.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println("[INFO] Registrazione: " + firstname + " " + lastname + ", " + email + " " + password);

        // se non esiste, ridirigo verso pagina di login con messaggio di errore
        switch (new UserDaoImpl().register(firstname, lastname, email, password)) {
            case -4:
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=Inserisci una email corretta (RFC-5321)");
                break;
            case -3:
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=Connessione SMPT fallita");
                break;
            case -2:
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=Alcuni campi sono rimasti vuoti");
                break;
            case -1:
            case 0:
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=Email gia' in uso");
                break;
            case 1:
                response.sendRedirect(request.getContextPath() + "/index.jsp?success=Registrazione effettuata! Controlla la mail " + email);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=Server-Side error");
                break;
        }
    }
}
