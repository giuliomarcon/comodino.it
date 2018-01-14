package servlet.autentication;

import daos.impl.UserDaoImpl;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * Gestisce il reset della password.
 */
@WebServlet(name = "PasswordResetServlet", urlPatterns = {"/passwordReset"})
public class PasswordResetServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (Utils.isNullOrEmpty(request.getParameter("token"))
                || Utils.isNullOrEmpty(request.getParameter("email"))) {
            response.sendRedirect("/index.jsp");
            return;
        }

        if (!Utils.isNullOrEmpty(request.getParameter("pwda")) && !Utils.isNullOrEmpty(request.getParameter("pwdb"))) {
            String token = request.getParameter("token"),
                    email = request.getParameter("email"),
                    pwda = request.getParameter("pwda"),
                    pwdb = request.getParameter("pwdb");
            System.out.println(token + "," + email + "," + pwda + "," + pwdb);
            if (!Objects.equals(pwda, pwdb)) {
                response.sendRedirect("/index.jsp?token=" + token + "&email=" + email + "&error=Le password non combaciano");
            } else if (new UserDaoImpl().resetPassword(token, email, pwda)) {
                response.sendRedirect("/index.jsp?success=" + URLEncoder.encode("La password è stata ripristinata con successo", "UTF-8"));
            } else {
                response.sendRedirect("/index.jsp");
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
