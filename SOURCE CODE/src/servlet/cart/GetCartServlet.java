package servlet.cart;

import main.Cart;
import main.CartItem;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Per un determinato utente si occupa di recuperare i prodotti aggiunti al carrello dal database.
 */
@WebServlet(name = "GetCartServlet", urlPatterns = {"/getcart"})
public class GetCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder ret = new StringBuilder();
        String size;

        if (request.getSession(false).getAttribute("user") == null) {
            Cookie[] cookies = request.getCookies();
            Cookie prodCookie = null;
            for (Cookie c : cookies) {
                if (c.getName().equals("cartproducts")) {
                    prodCookie = c;
                }
            }
            if (prodCookie == null) {
                ret = new StringBuilder("<li class=\"text-center\"><a>Carrello vuoto...</a></li>");
                size = "<span class=\"badge\">\n" +
                        "                            <i class=\"fa fa-shopping-cart\" aria-hidden=\"true\"></i> 0\n" +
                        "                        </span>\n" +
                        "                        &nbsp;&nbsp;Carrello <span class=\"caret\"></span>";

            } else {
                String[] cartproducts = prodCookie.getValue().split("\\|");
                int totalSize = 0;
                for (String prod : cartproducts) {
                    String[] prod_infos = prod.split("_");
                    totalSize += Integer.parseInt(prod_infos[3]);
                }
                //System.out.println("LENGHT: " +cartproducts.length);
                size = "<span class=\"badge\">\n" +
                        "                            <i class=\"fa fa-shopping-cart\" aria-hidden=\"true\"></i> " + totalSize + "\n" +
                        "                        </span>\n" +
                        "                        &nbsp;&nbsp;Carrello <span class=\"caret\"></span>";

                for (String cartItem : cartproducts) {
                    String[] pieces = cartItem.split("_");
                    ret.append("<li><a href=\"/product.jsp?product=").append(pieces[0]).append("&shop=").append(pieces[2]).append("\"> <b>").append(pieces[1].replace("-", " ")).append("</b> - ").append(pieces[3]).append("&nbsp;pz</a></li>");
                }
                ret.append("<li class=\"divider\"></li>\n" + "                        <li class=\"text-center\"><a href=\"\" role=\"button\" data-toggle=\"modal\" data-target=\"#LoginSignup\">Loggati per continuare <i class=\"fa fa-angle-double-right\" aria-hidden=\"true\"></i>\n</a></li>");
            }
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            if (request.getParameter("type").equals("drop"))
                response.getWriter().write(size);
            else
                response.getWriter().write(ret.toString());
            return;
        }


        Cart cart;
        if (request.getParameter("type").equals("drop"))
            cart = ((User) request.getSession(false).getAttribute("user")).getCart(); // con update del carrello
        else
            cart = ((User) request.getSession(false).getAttribute("user")).getCart(false); // senza update del carrello

        size = "<span class=\"badge\">\n" +
                "                            <i class=\"fa fa-shopping-cart\" aria-hidden=\"true\"></i> " + cart.totalSize() + "\n" +
                "                        </span>\n" +
                "                        &nbsp;&nbsp;Carrello <span class=\"caret\"></span>";

        if (cart.size() == 0) {
            ret = new StringBuilder("<li class=\"text-center\"><a>Carrello vuoto...</a></li>");
        } else {
            for (CartItem cartItem : cart) {
                ret.append("<li><a href=\"/product.jsp?product=").append(cartItem.getProduct().getProductID()).append("&shop=").append(cartItem.getProduct().getShopID()).append("\"> <b>").append(cartItem.getProduct().getProductName()).append("</b> - ").append(cartItem.getQuantity()).append("&nbsp;pz</a></li>");
            }
            ret.append("<li class=\"divider\"></li>\n" + "                        <li class=\"text-center\"><a href=\"/restricted/cart.jsp\">Vedi carrello <i class=\"fa fa-angle-double-right\" aria-hidden=\"true\"></i>\n</a></li>");
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        if (request.getParameter("type").equals("drop"))
            response.getWriter().write(size);
        else
            response.getWriter().write(ret.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
