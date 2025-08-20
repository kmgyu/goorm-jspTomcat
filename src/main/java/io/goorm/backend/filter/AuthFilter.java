package main.java.io.goorm.backend.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/board/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String command = request.getParameter("command");

        // 로그인이 필요한 명령어들
        if (isLoginRequired(command)) {
            HttpSession session = httpRequest.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                httpResponse.sendRedirect("front?command=login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isLoginRequired(String command) {
        return "boardWrite".equals(command) ||
                "boardUpdate".equals(command) ||
                "boardDelete".equals(command);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}