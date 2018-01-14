package servlet.search;

import daos.impl.ProductDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Visualizza l'autocompletamento durante la digitazione.
 */
@WebServlet(name = "SearchAutocompleteServlet", urlPatterns = {"/searchautocomplete"})
public class SearchAutocompleteServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String products = new ProductDaoImpl().getAutocompleteProducts(request.getParameter("term"));

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(products);
    }
}
