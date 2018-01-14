package filters;
import main.Cart;
import main.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class CheckoutFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("[INFO] Checkout filter entered");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        User user = (User) req.getSession(false).getAttribute("user");
        Cart cart = user.getCart(true);
        if(cart.totalSize() == 0){
            res.sendRedirect("/index.jsp?warning="+URLEncoder.encode("Il carrello è vuoto","UTF-8"));
        }
        else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}