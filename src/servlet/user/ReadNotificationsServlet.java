package servlet.user;

import daos.impl.NotificationDaoImpl;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Recupera le notifiche disponibili per l'utente.
 */
@WebServlet(name = "ReadNotificationsServlet", urlPatterns = {"/restricted/readnotifications"})
public class ReadNotificationsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        boolean result = new NotificationDaoImpl().readNotifications(user);
        if (!result) {
            response.sendRedirect("addresses.jsp?error=Errore in fase di inserimento, riprova.");
            return;
        }
        response.sendRedirect("addresses.jsp?success=Indirizzo aggiunto!");

    }
}