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
import java.io.IOException;

/**
 * Si occupa, per un negozio online, di aggiungere una locazione fisica
 */
@WebServlet(name = "AddPhysicalShopServlet", urlPatterns = {"/restricted/vendor/addphysicalshop"})
public class AddPhysicalShopServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        PhysicalShop physhop = new PhysicalShop();
        physhop.setAddress(request.getParameter("ShopAddress"));
        physhop.setCity(request.getParameter("ShopCity"));
        physhop.setZip(request.getParameter("ShopCAP"));
        physhop.setState(request.getParameter("ShopCountry"));
        physhop.setOpeninghours(request.getParameter("ShopHours"));
        physhop = Utils.updateGPSCoords(physhop);
        HttpSession session = request.getSession(false);
        Shop shop = (Shop) session.getAttribute("shop");
        ShopDao shopDao = new ShopDaoImpl();
        if (shopDao.addPhysicalShop(shop.getShopID(), physhop)) {
            request.getSession().setAttribute("shop", shopDao.getShop(shop.getShopID()));
            response.sendRedirect("/restricted/vendor/shop_panel.jsp?success=Negozio fisico creato con successo");
        } else {
            response.sendRedirect("/restricted/profile.jsp?warning=Creazione negozio fisico non riuscita");
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
