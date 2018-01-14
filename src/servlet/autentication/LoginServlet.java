package servlet.autentication;

import daos.impl.ShopDaoImpl;
import daos.impl.UserDaoImpl;
import main.Shop;
import main.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Si occupa di effettuare il login dell'utente e creare una sessione date delle credenziali valide.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (request.getParameter("email") == null || request.getParameter("password") == null) {
            response.sendRedirect("/index.jsp?error=Parametri Mancanti");
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println("Parametri: " + email + " " + password);

        // controllo nel DB se esiste un utente con lo stesso username + password
        User user = new UserDaoImpl().authUser(email, password);
        // se non esiste, ridirigo verso pagina di login con messaggio di errore
        if (user == null) {
            // TODO: bruttissimo che non sappia la differenza tra username/password errati e email ancora da verificare
            response.sendRedirect("/index.jsp?error=Username o password errati");
        } else {
            // imposto l'utente connesso come attributo di sessione
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            if (user.hasShop()) {
                Shop shop = new ShopDaoImpl().getShop(user.getShopID());
                session.setAttribute("shop", shop);
            }
            // mando un redirect alla servlet che carica i prodotti

            //converto il cart anonimo dei cookie in un cart di sessione utente (database)

            Cookie cartproducts = null;
            int productsAdded = 0;
            switch ((productsAdded = new UserDaoImpl().cookieToCart(user, request.getCookies()))) {
                case -1:
                    response.sendRedirect("/index.jsp");
                    break;
                case 0:
                    response.setContentType("text/html");
                    cartproducts = new Cookie("cartproducts", "");
                    cartproducts.setMaxAge(0);
                    cartproducts.setPath("/");
                    response.addCookie(cartproducts);
                    response.sendRedirect("/index.jsp?warning=Non e' stato aggiunto nessun nuovo articolo");
                    break;
                default:
                    response.setContentType("text/html");
                    cartproducts = new Cookie("cartproducts", "");
                    cartproducts.setMaxAge(0);
                    cartproducts.setPath("/");
                    response.addCookie(cartproducts);

                    response.sendRedirect("/index.jsp?success=" +
                            (productsAdded == 1 ? "Il nuovo articolo e' stato aggiunto" : productsAdded + " nuovi articoli sono stati aggiunti") +
                            " al carrello");
                    break;
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp?error=Percorso invalido");
    }
}
