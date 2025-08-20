package io.goorm.backend.command;

import io.goorm.backend.Board;
import io.goorm.backend.BoardDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BoardUpdateCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String idStr = request.getParameter("id");

            // 로그인 확인
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect("front?command=login");
                return null;
            }

            if (request.getMethod().equals("GET")) {
                // 수정 폼 표시
                if (idStr == null || idStr.trim().isEmpty()) {
                    request.setAttribute("error", "게시글 ID가 필요합니다.");
                    return "/board/list.jsp";
                }

                long id = Integer.parseInt(idStr);
                BoardDAO dao = new BoardDAO();
                Board board = dao.getBoardById(id);

                // board 존재 체크
                if (board == null) {
                    request.setAttribute("error", "존재하지 않는 게시글입니다.");
                    return "/board/list.jsp";
                }

                // 작성자 확인
                int currentUserId = (Integer) session.getAttribute("userId");
                if (board.getWriterId() != currentUserId) {
                    request.setAttribute("error", "본인이 작성한 글만 수정할 수 있습니다.");
                    return "/board/view.jsp";
                }

                request.setAttribute("board", board);
                return "/board/update.jsp";

            } else {
                // POST 요청 - 수정 처리
                request.setCharacterEncoding("UTF-8");

                long id = Integer.parseInt(idStr);
                String title = request.getParameter("title");
                String content = request.getParameter("content");

                if (title == null || title.trim().isEmpty()) {
                    request.setAttribute("error", "제목을 입력해주세요.");
                    return "/board/update.jsp";
                }

                Board board = new Board();
                board.setId(id);
                board.setTitle(title);
                board.setContent(content);
                board.setWriterId((Integer) session.getAttribute("userId"));

                BoardDAO dao = new BoardDAO();
                Board validBoard = dao.getBoardById(id);

                // 작성자 확인
                int currentUserId = board.getWriterId();
                if (validBoard.getWriterId() != currentUserId) {
                    request.setAttribute("error", "본인이 작성한 글만 수정할 수 있습니다.");
                    return "/board/view.jsp";
                }

                dao.updateBoard(board);

                // 수정 후 상세보기로 이동
                response.sendRedirect("front?command=boardView&id=" + id);
                return null;
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "잘못된 게시글 ID입니다.\n" + e);
            return "/board/list.jsp";
        } catch (Exception e) {
            request.setAttribute("error", "게시글 수정에 실패했습니다: " + e.getMessage());
            return "/board/list.jsp";
        }
    }
}