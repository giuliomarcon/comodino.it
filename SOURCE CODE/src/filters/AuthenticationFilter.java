package filters;

import main.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //System.out.println("[INFO] Auth Filter: Entered");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpSession session  = req.getSession(false);

        if (session == null){
            System.out.println("[INFO] Auth Filter: Nessuna sessione esistente");
            res.sendRedirect("/");
        }
        else if( session.getAttribute("user") == null) {
            System.out.println("[INFO] Auth Filter: L'utente non è loggato");
            res.sendRedirect("/");
        }
        else {
            User u = (User)session.getAttribute("user");
            if (u.getPrivacy() == 0){
                System.out.println("Privacy not accepted yet.");
                String path = req.getRequestURI();
                if (!path.endsWith("profile.jsp") && !path.endsWith("/acceptprivacy") && !path.endsWith("/logout")) {
                    res.sendRedirect("profile.jsp?error=Privacy non accettata");
                    return;
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}