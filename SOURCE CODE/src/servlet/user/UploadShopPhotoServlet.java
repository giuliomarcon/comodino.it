package servlet.user;


import daos.ShopDao;
import daos.impl.ShopDaoImpl;
import main.Shop;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Permette di aggiungere una foto al negozio da parte dell'utente o del venditore stesso.
 */
@MultipartConfig
@WebServlet(name = "UploadShopPhotoServlet", urlPatterns = {"/restricted/uploadshopphoto"})
public class UploadShopPhotoServlet extends HttpServlet {

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part shopPhoto = request.getPart("shopPhoto");
        int shopID = Integer.parseInt(request.getParameter("shopID"));
        if (shopPhoto.getSize() > 0) {
            HttpSession session = request.getSession(false);
            Shop shop = (Shop) session.getAttribute("shop");
            ShopDao shopDao = new ShopDaoImpl();
            if (shopDao.addShopPhoto(shopID, shopPhoto)) {
                response.sendRedirect(request.getHeader("referer"));
            } else
                response.sendRedirect("index.jsp?error=Upload non riuscito");
        } else response.sendRedirect("index.jsp?warning=Nessuna foto selezionata");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
