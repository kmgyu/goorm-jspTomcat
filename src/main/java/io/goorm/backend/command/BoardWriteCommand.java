package io.goorm.backend.command;

import io.goorm.backend.Board;
import io.goorm.backend.BoardDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

public class BoardWriteCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (request.getMethod().equals("GET")) {
                // 로그인 확인
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("user") == null) {
                    // 로그인하지 않은 사용자는 로그인 페이지로 이동
                    response.sendRedirect("front?command=login");
                    return null;
                }
                return "/board/write.jsp";
            } else {
                // POST 요청 - 게시글 작성
                request.setCharacterEncoding("UTF-8");

                // 로그인 확인
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("user") == null) {
                    response.sendRedirect("front?command=login");
                    return null;
                }

                String title = request.getParameter("title");
                String content = request.getParameter("content");

                if (title == null || title.trim().isEmpty() ||
                        content == null || content.trim().isEmpty()) {
                    request.setAttribute("error", "제목과 내용을 모두 입력해주세요.");
                    return "/board/write.jsp";
                }

                Board board = new Board();
                board.setTitle(title);
                board.setContent(content);
                board.setWriterId((Integer) session.getAttribute("userId"));
                board.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                BoardDAO boardDAO = new BoardDAO();
                boardDAO.insertBoard(board);

                // 목록으로 리다이렉트
                response.sendRedirect("front?command=boardList");
                return null; // 리다이렉트 시 null 반환
            }
        } catch (Exception e) {
            request.setAttribute("error", "게시글 작성 중 오류가 발생했습니다." + e.getMessage());
            return "/board/write.jsp";
        }
    }
}