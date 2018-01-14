package servlet.user;

import daos.UserDao;
import daos.impl.UserDaoImpl;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * Si occupa di cambiare la password per l'utente loggato
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/restricted/changepassword"})
public class ChangePasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String curPwd = request.getParameter("CurrentPassword");
        String newPwd = request.getParameter("NewPassword");
        String repeatPwd = request.getParameter("RepeatPassword");
        System.out.println("Parametri: " + curPwd + " " + newPwd + " " + repeatPwd);
        if (Objects.equals(newPwd, repeatPwd)) {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");
            UserDao userDao = new UserDaoImpl();
            if (userDao.changePwd(user, curPwd, newPwd)) {
                System.out.println("[ " + user.getFirstName() + " ] Password modificata");
                response.sendRedirect("/restricted/profile.jsp?success=Password modificata");
            } else {
                response.sendRedirect("/restricted/profile.jsp?error=Password attuale errata");
            }
        } else {
            response.sendRedirect("/restricted/profile.jsp?error=Le nuove password non corrispondono");
        }

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
