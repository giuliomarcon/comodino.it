package filters;

import main.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("[INFO] Admin Filter: Entered");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpSession session  = req.getSession(false);

        if (session == null){
            System.out.println("[INFO] Admin Filter: Nessuna sessione esistente");
            res.sendRedirect("/");
        }
        else if( session.getAttribute("user") == null) {
            System.out.println("[INFO] Admin Filter: L'utente non è loggato");
            res.sendRedirect("/");
        }
        else {
            User u = (User)session.getAttribute("user");
            if (u.getType() == 0){
                System.out.println("    L'utente non è admin.");
                res.sendRedirect("/restricted/profile.jsp?error=Non disponi dei privilegi per questa sezione");
                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}