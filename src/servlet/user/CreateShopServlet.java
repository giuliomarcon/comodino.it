package servlet.user;

import daos.impl.ShopDaoImpl;
import main.User;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Permette a un utente "standard" di aprire un negozio online e diventare venditore.
 * Prevede inoltre di includere un negozio fisico.
 */
@WebServlet(name = "CreateShopServlet", urlPatterns = "/restricted/createshop")
public class CreateShopServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        User user = (User) request.getSession(false).getAttribute("user");
        if (user.hasShop()) {
            System.out.println("[INFO] Redirect to private shop panel");
            response.sendRedirect("/index.jsp?warning=Impossibile aprire piu' di un negozio per utente");
            return;
        }

        String shopName, shopDescription, shopWebsite,
                shopAddress, shopState, shopOpeningHours, shopZIP, shopCity;

        if (
                Utils.isNullOrEmpty(shopName = request.getParameter("shop-name")) ||
                        Utils.isNullOrEmpty(shopDescription = request.getParameter("shop-description")) ||
                        Utils.isNullOrEmpty(shopWebsite = request.getParameter("shop-website"))
                ) {
            response.sendRedirect("createshop.jsp?error=Campi non compilati nella Sezione Negozio");
            return;
        }

        if ( // tutti i campi del negozio fisico sono riempiti (creo negozio fisico)
                !Utils.isNullOrEmpty(shopAddress = request.getParameter("shop-address")) &
                        !Utils.isNullOrEmpty(shopCity = request.getParameter("shop-city")) &
                        !Utils.isNullOrEmpty(shopState = request.getParameter("shop-state")) &
                        !Utils.isNullOrEmpty(shopZIP = request.getParameter("shop-ZIP")) &
                        !Utils.isNullOrEmpty(shopOpeningHours = request.getParameter("shop-openingHours"))
                ) {
            System.out.println("[INFO] Tutti i campi del negozio fisico sono riempiti (ora creo negozio fisico)");
            ArrayList<Float> latlong = Utils.updateGPSCoords(shopAddress, shopCity, shopZIP);
            if(latlong == null){
                response.sendRedirect("vendor/shop_panel.jsp?error=Inserisci un indirizzo valido");
                return;
            }
            Float shopLatitude = latlong.get(0);
            Float shopLongitude = latlong.get(1);
            boolean result = new ShopDaoImpl().createNewPhysicalShop(
                    user, shopName, shopDescription,
                    shopWebsite, shopAddress, shopCity, shopState, shopZIP, shopOpeningHours, shopLatitude, shopLongitude);
            if (result){ // due if per dare sincronia alle operazioni
                request.getSession().setAttribute("shop", new ShopDaoImpl().getShop(user.getShopID()));
            }
            if (result)
                response.sendRedirect("vendor/shop_panel.jsp");
            else
                response.sendRedirect("createshop.jsp?error=Errore creazione negozio fisico");
            return;
        } else if (!Utils.isNullOrEmpty(shopAddress) ||
                !Utils.isNullOrEmpty(shopCity) ||
                !Utils.isNullOrEmpty(shopState) ||
                !Utils.isNullOrEmpty(shopZIP) ||
                !Utils.isNullOrEmpty(shopOpeningHours)) {
            response.sendRedirect("createshop.jsp?error=Campi non compilati nella sezione negozio fisico");
            return;
        }
        int result = new ShopDaoImpl().createNewShop(user, shopName, shopDescription, shopWebsite);
        if (result != 0){ // due if per dare sincronia alle operazioni
            request.getSession().setAttribute("shop", new ShopDaoImpl().getShop(user.getShopID()));
        }
        if (result != 0)
            response.sendRedirect("vendor/shop_panel.jsp");
        else
            response.sendRedirect("createshop.jsp?error=Errore creazione negozio online");
    }
}
