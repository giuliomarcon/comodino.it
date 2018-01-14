package servlet.cart;

import daos.impl.ProductDaoImpl;
import daos.impl.UserDaoImpl;
import main.Product;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Si occupa di aggiungere il prodotto selezionato al carrello,
 * se l'utente non è loggato crea un carrello temporaneo tramite cookies
 */
@WebServlet(name = "AddCartItemServlet", urlPatterns = {"/addcartitem"})
public class AddCartItemServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        int productID = Integer.parseInt(request.getParameter("productID"));
        int shopID = Integer.parseInt(request.getParameter("shopID"));
        if (user != null) {
            new UserDaoImpl().addCartItem(user, productID, shopID);
        } else { // se l'utente è anonimo
            Cookie products = null;
            Cookie[] cookies = request.getCookies();
            for (Cookie c : cookies) {
                if (c.getName().equals("cartproducts")) {
                    products = c;
                }
            }
            // l'utente anonimo non ha ancora alcun prodotto nel carrello
            if (products == null) {
                Product p = new ProductDaoImpl().getProduct(productID, shopID);
                // format: 51_Tavolo della nonna_3_1,25_Lampada ad olio_8_2
                products = new Cookie("cartproducts", p.getProductID() + "_" + p.getProductName().replace(" ", "-") + "_" + p.getShopID() + "_1");
                response.addCookie(products);
            } else {
                String res = products.getValue();
                Product prod = new ProductDaoImpl().getProduct(productID, shopID);
                String regex = "(?<=" + prod.getProductID() + "_" + prod.getProductName().replace(" ", "-") + "_" + prod.getShopID() + "_)\\d+";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(res);
                if (m.find()) { // il prodotto è già nel carrello
                    // we're only looking for one group, so get it
                    String theGroup = m.group(0);
                    System.out.println("Trovata quantità: " + theGroup);
                    theGroup = Integer.toString(Integer.parseInt(theGroup) + 1);
                    System.out.println("Aumentata quantità: " + theGroup);
                    res = res.replaceAll(regex, theGroup);
                } else {
                    res = res.concat("|" + prod.getProductID() + "_" + prod.getProductName().replace(" ", "-") + "_" + prod.getShopID() + "_1");
                }
                res = res.replace(" ", "-");
                System.out.println(res);
                products.setValue(res);
                response.addCookie(products);
                /*
                String[] prodList = products.getValue().split("\|");
                for (String line:prodList) {

                    String[] p = line.split("_");
                    if (p[0].equals(String.valueOf(productID)) && p[2].equals(String.valueOf(shopID))){
                        int q = Integer.parseInt(p[3]) + 1;
                        p[2] = String.valueOf(q);
                    }

                }
                */
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}