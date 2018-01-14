package servlet.user;

import daos.ProductDao;
import daos.ReviewDao;
import daos.ShopDao;
import daos.impl.NotificationDaoImpl;
import daos.impl.ProductDaoImpl;
import daos.impl.ReviewDaoImpl;
import daos.impl.ShopDaoImpl;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Per un prodotto acquistato permette all'utente di lasciare una recensione sul prodotto e sul negozio.
 */
@WebServlet(name = "OpenReviewServlet", urlPatterns = {"/restricted/openreview"})
public class OpenReviewServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        System.out.println("[INFO] OpenReview Servlet: Entered");
        if (request.getParameter("stitle") == null || request.getParameter("sdescription") == null || request.getParameter("srating") == null || request.getParameter("shopID") == null) {
            response.sendRedirect("/index.jsp?error=Parametri Negozio Mancanti");
            return;
        }
        String stitle = request.getParameter("stitle");
        String sdescription = request.getParameter("sdescription");
        System.out.println(sdescription);
        int srating = 1;
        try {
            srating = Integer.parseInt(request.getParameter("srating"));
        } catch (Exception ignored) {
        }

        int shopID = Integer.parseInt(request.getParameter("shopID"));
        int userID = ((User) request.getSession(false).getAttribute("user")).getUserID();

        System.out.println("\tDispute ProductID: " + shopID + " UserID: " + userID +
                "\n\tTitle: " + stitle +
                "\n\tDescription: " + sdescription +
                "\n\tRating: " + srating
        );

        ReviewDao reviewDao = new ReviewDaoImpl();
        boolean result;
        int newReviewID = reviewDao.createShopReview(stitle, sdescription, srating, shopID, userID);
        if (newReviewID != 0) {
            result = new NotificationDaoImpl().createShopReviewNotification(userID, shopID, stitle, srating);
            ShopDao pd = new ShopDaoImpl();
            result = result && pd.updateShopRating(shopID);
        } else { // la creazione della recensione è fallita
            response.sendRedirect("/restricted/orderhistory.jsp?error=Errore Creazione Recensione Negozio");
            return;
        }
        if (!result) { // la creazione della notifica della disputa è fallita
            response.sendRedirect("/restricted/orderhistory.jsp?error=Errore Creazione Notifica Recensione Negozio");
            return;
        }
        System.out.println("[INFO] OpenReview Servlet: Shop review created.");

        //INIZIO PRODUCT REVIEW

        if (request.getParameter("ptitle") == null || request.getParameter("pdescription") == null || request.getParameter("prating") == null || request.getParameter("productID") == null) {
            response.sendRedirect("/restricted/orderhistory.jsp?error=Parametri Prodotto Mancanti");
            return;
        }
        String ptitle = request.getParameter("ptitle");
        String pdescription = request.getParameter("pdescription");
        System.out.println(pdescription);
        int prating = 1;
        try {
            prating = Integer.parseInt(request.getParameter("prating"));
        } catch (Exception ignored) {
        }

        int productID = Integer.parseInt(request.getParameter("productID"));

        System.out.println("\tDispute ProductID: " + productID + " UserID: " + userID +
                "\n\tTitle: " + ptitle +
                "\n\tDescription: " + pdescription +
                "\n\tRating: " + prating
        );
        boolean result2;
        ReviewDao reviewDao2 = new ReviewDaoImpl();
        int newReviewID2 = reviewDao2.createProductReview(ptitle, pdescription, prating, productID, userID);
        if (newReviewID2 != 0) {
            result2 = new NotificationDaoImpl().createProductReviewNotification(newReviewID2, ptitle, prating);
            ProductDao pd = new ProductDaoImpl();
            result2 = result2 && pd.updateProductRating(productID);
        } else { // la creazione della recensione è fallita
            response.sendRedirect("/restricted/orderhistory.jsp?error=Errore Creazione Recensione Prodotto");
            return;
        }
        if (!result2) { // la creazione della notifica della disputa è fallita
            response.sendRedirect("/restricted/orderhistory.jsp?error=Errore Creazione Notifica Recensione Prodotto");
            return;
        }
        //FINE PRODUCT REVIEW
        response.sendRedirect("/restricted/orderhistory.jsp?success=Recensione creata!");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
