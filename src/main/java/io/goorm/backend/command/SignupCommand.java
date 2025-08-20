package io.goorm.backend.command;

import io.goorm.backend.User;
import io.goorm.backend.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;

public class SignupCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (request.getMethod().equals("GET")) {
                // 회원가입 폼 표시
                return "/user/signup.jsp";
            } else {
                // POST 요청 - 회원가입 처리
                request.setCharacterEncoding("UTF-8");

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String name = request.getParameter("name");
                String email = request.getParameter("email");

                // 유효성 검사
                if (username == null || username.trim().isEmpty() ||
                        password == null || password.trim().isEmpty() ||
                        name == null || name.trim().isEmpty()) {
                    request.setAttribute("error", "필수 필드를 모두 입력해주세요.");
                    return "/user/signup.jsp";
                }

                // 중복 사용자명 확인
                UserDAO userDAO = new UserDAO();
                if (userDAO.getUserByUsername(username) != null) {
                    request.setAttribute("error", "이미 사용 중인 아이디입니다.");
                    return "/user/signup.jsp";
                }

                // 사용자 생성 및 저장
                User user = new User();
                user.setUsername(username);
                user.setPassword(hashPassword(password)); // 비밀번호 해시
                user.setName(name);
                user.setEmail(email);

                if (userDAO.insertUser(user)) {
                    // 회원가입 성공 - 로그인 페이지로 이동
                    response.sendRedirect("front?command=login");
                    return null;
                } else {
                    request.setAttribute("error", "회원가입에 실패했습니다.");
                    return "/user/signup.jsp";
                }
            }

        } catch (Exception e) {
            request.setAttribute("error", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "/user/signup.jsp";
        }
    }

    // 간단한 비밀번호 해시 함수 (실제 운영에서는 bcrypt 등 사용)
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
            return password; // 해시 실패 시 원본 반환
        }
    }
}