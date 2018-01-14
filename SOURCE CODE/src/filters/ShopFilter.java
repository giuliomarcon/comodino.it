package filters;

import daos.ShopDao;
import daos.impl.ShopDaoImpl;
import main.ProductGroup;
import main.Shop;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class ShopFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        if(req.getParameter("id") == null){
            System.out.println("URL " + req.getRequestURI());
            res.sendRedirect("/index.jsp");
            return;
        }

        ShopDao shopDao = new ShopDaoImpl();
        Shop shop = shopDao.getShop(Integer.parseInt(req.getParameter("id")));
        HashMap<String, ProductGroup> shopProducts = shopDao.getShopProducts(req.getParameter("id"));
        //in caso di bug prova a cambiare il nome dell'attributo


        if (shop == null){
            System.out.println("URL " + req.getRequestURI());
            res.sendRedirect("/index.jsp?error=Errore caricamento negozio");
        }
        else {
            req.setAttribute("shop", shop);
            req.setAttribute("shopproducts", shopProducts);

            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}