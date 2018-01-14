package servlet.user;


import daos.UserDao;
import daos.impl.UserDaoImpl;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Permette di aggiungere la foto profilo dell'utente.
 */
@MultipartConfig
@WebServlet(name = "UploadUserPhotoServlet", urlPatterns = {"/restricted/uploaduserphoto"})
public class UploadUserPhotoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part userPhoto = request.getPart("userPhoto");
        if (userPhoto.getSize() > 0) {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");
            UserDao userDao = new UserDaoImpl();
            if (userDao.addUserPhoto(user, userPhoto)) {
                user.setProfilePhoto(userDao.getUserPhoto(user));
                response.sendRedirect("/restricted/profile.jsp?success=Foto caricata con successo!");
            } else
                response.sendRedirect("/restricted/profile.jsp?error="+ URLEncoder.encode("File troppo grande [Max 4 MB]","UTF-8"));
        } else response.sendRedirect("/restricted/profile.jsp?warning=Nessuna foto selezionata");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
