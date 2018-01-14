package servlet.search;

import daos.ProductDao;
import daos.impl.ProductDaoImpl;
import main.ProductGroup;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Data una query di ricerca si occupa di recuperare e visualizzare i prodotti più similim
 * gestendo eventuali filtri di ricerca.
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        response.setContentType("text/html");
        //PrintWriter out = response.getWriter();

        //Quick check
        if (request.getParameter("q") == null) {
            return;
        }

        try {
            ProductDao productDao = new ProductDaoImpl();
            HashSet<String> vendors = new HashSet<>();
            HashSet<String> categories = new HashSet<>();
            HashSet<String> geozone = new HashSet<>();

            //Map
            List<Map.Entry<String, ProductGroup>> products = productDao.getProducts(request.getParameterMap());
            request.setAttribute("products", products);
            for (Map.Entry<String, ProductGroup> entry : products) {
                ProductGroup i = entry.getValue();

                for (int j = 0; j < i.getVendors().size(); j++)
                    vendors.add(i.getVendors().get(j).getName());

                for (int j = 0; j < i.getList().size(); j++)
                    categories.add(i.getList().get(j).getCategoryName());

                for (int j = 0; j < i.getGeo().size(); j++)
                    geozone.add(i.getGeo().get(j));

            }


            //ArrayList
            request.setAttribute("categories", categories);
            request.setAttribute("vendors", vendors);
            request.setAttribute("geozone", geozone);

            //redirect
            RequestDispatcher view = request.getRequestDispatcher("/search.jsp");
            view.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
