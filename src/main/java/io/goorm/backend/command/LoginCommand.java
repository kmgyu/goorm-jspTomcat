package io.goorm.backend.command;

import io.goorm.backend.User;
import io.goorm.backend.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;

public class LoginCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (request.getMethod().equals("GET")) {
                // 로그인 폼 표시
                return "/user/login.jsp";
            } else {
                // POST 요청 - 로그인 처리
                request.setCharacterEncoding("UTF-8");

                String username = request.getParameter("username");
                String password = request.getParameter("password");

                // 유효성 검사
                if (username == null || username.trim().isEmpty() ||
                        password == null || password.trim().isEmpty()) {
                    request.setAttribute("error", "아이디와 비밀번호를 모두 입력해주세요.");
                    return "/user/login.jsp";
                }

                // 비밀번호 해시
                String hashedPassword = hashPassword(password);

                // 로그인 검증
                UserDAO userDAO = new UserDAO();
                User user = userDAO.login(username, hashedPassword);

                if (user != null) {
                    // 로그인 성공 - 세션에 사용자 정보 저장
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("userName", user.getName());

                    // 게시판 목록으로 이동
                    response.sendRedirect("front?command=boardList");
                    return null;
                } else {
                    // 로그인 실패
                    request.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
                    return "/user/login.jsp";
                }
            }

        } catch (Exception e) {
            request.setAttribute("error", "로그인 처리 중 오류가 발생했습니다.");
            return "/user/login.jsp";
        }
    }

    // 비밀번호 해시 함수 (SignupCommand와 동일)
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }
}