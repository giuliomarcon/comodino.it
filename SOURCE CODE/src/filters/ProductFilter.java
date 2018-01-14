package filters;

import daos.impl.ProductDaoImpl;
import main.Product;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/product.jsp")
public class ProductFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        if(req.getParameter("product") == null || req.getParameter("shop") == null){
            System.out.println("URL " + req.getRequestURI());
            res.sendRedirect("/index.jsp");
            return;
        }

        Product product = new ProductDaoImpl().getProduct(Integer.parseInt(req.getParameter("product")),Integer.parseInt(req.getParameter("shop")));

        if (product == null){
            System.out.println("URL " + req.getRequestURI());
            res.sendRedirect("/index.jsp");
        }
        else {
            req.setAttribute("product",product);
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}