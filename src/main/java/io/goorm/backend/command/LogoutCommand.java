package io.goorm.backend.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 세션 무효화
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // 게시판 목록으로 이동
            response.sendRedirect("front?command=boardList");
            return null;

        } catch (Exception e) {
            // 에러 발생 시에도 게시판 목록으로 이동
            try {
                response.sendRedirect("front?command=boardList");
            } catch (Exception ex) {
                // 최후의 수단으로 에러 페이지 반환
                return "/error.jsp";
            }
            return null;
        }
    }
}