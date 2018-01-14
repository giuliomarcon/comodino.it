package filters;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "CrossScriptingFilter")
public class CrossScriptingFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(new RequestWrapper((HttpServletRequest) req), resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
