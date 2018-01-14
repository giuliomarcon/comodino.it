package filters;
import main.Cart;
import main.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class PaymentFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("[INFO] Payment filter entered");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        User user = (User) req.getSession(false).getAttribute("user");
        Cart cart = user.getCart(false);
        System.out.println(req.getHeader("Referer"));
        if(cart == null){
            res.sendRedirect("/index.jsp?warning="+URLEncoder.encode("Il carrello è vuoto","UTF-8"));
        }
        else if(req.getHeader("Referer") == null){
            res.sendRedirect("/index.jsp");
        }
        else if(!req.getHeader("Referer").contains("/restricted/checkout.jsp")){
            res.sendRedirect("/restricted/checkout.jsp?warning="+URLEncoder.encode("Conferma le modalità di spedizione prima di procedere al pagamento","UTF-8"));
        }
        else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}