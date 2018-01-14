package servlet.user.vendor;

import daos.ShopDao;
import daos.impl.ShopDaoImpl;
import main.PhysicalShop;
import main.Shop;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Permette di modificare le informazioni di base di un negozio online e,
 * se presente, del relativo negozio fisico.
 */
@WebServlet(name = "EditShopInfoServlet", urlPatterns = {"/restricted/vendor/editshopinfo"})
public class EditShopInfoServlet extends HttpServlet {

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
        request.setCharacterEncoding("UTF-8");
        String shopName = request.getParameter("ShopName");
        String shopDescription = request.getParameter("ShopDescription");
        String shopWebsite = request.getParameter("ShopWebsite");
        String shopAddress = request.getParameter("ShopAddress");
        String shopCity = request.getParameter("ShopCity");
        String shopCAP = request.getParameter("ShopCAP");
        String shopState = request.getParameter("ShopState");
        String shopHours = request.getParameter("ShopHours");
        System.out.println("Parametri: " + shopName + " " + shopDescription + " " + shopWebsite + " " + shopAddress + " " + shopCity + " " + shopCAP);
        HttpSession session = request.getSession(false);
        Shop shop = (Shop) session.getAttribute("shop");
        boolean isPhysical = false;
        if (!shopName.isEmpty()) {
            shop.setName(shopName);
        }
        if (!shopDescription.isEmpty()) {
            shop.setDescription(shopDescription);
        }
        if (!shopWebsite.isEmpty()) {
            shop.setWebsite(shopWebsite);
        }
        if (shop instanceof PhysicalShop) {
            isPhysical = true;
            if (!shopAddress.isEmpty()) {
                ((PhysicalShop) shop).setAddress(shopAddress);
            }
            if (!shopCity.isEmpty()) {
                ((PhysicalShop) shop).setCity(shopCity);
            }
            if (!shopCAP.isEmpty()) {
                ((PhysicalShop) shop).setZip(shopCAP);
            }
            if (!shopState.isEmpty()) {
                ((PhysicalShop) shop).setState(shopState);
            }
            if (!shopHours.isEmpty()) {
                ((PhysicalShop) shop).setOpeninghours(shopHours);
            }
            Utils.updateGPSCoords((PhysicalShop) shop);
        }
        ShopDao shopDao = new ShopDaoImpl();
        if (isPhysical) {
            if (shopDao.editPhysicalInfo((PhysicalShop) shop)) {
                System.out.println("[ " + shop.getName() + " ] Info modificate");
                response.sendRedirect("shop_panel.jsp?success=Info aggiornate");
            } else {
                response.sendRedirect("shop_panel.jsp?warning=Info non aggiornate");
            }
        } else {
            if (shopDao.editInfo(shop)) {
                System.out.println("[ " + shop.getName() + " ] Info modificate");
                response.sendRedirect("shop_panel.jsp?success=Info aggiornate");
            } else {
                response.sendRedirect("shop_panel.jsp?warning=Info non aggiornate");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
