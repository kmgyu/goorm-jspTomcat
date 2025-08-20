package io.goorm.backend.command;

import io.goorm.backend.User;
import io.goorm.backend.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;

public class UserInfoCommand implements Command{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect("front?command=login");
                return null;
            }

            Integer userId = (Integer) session.getAttribute("userId");

            UserDAO dao = new UserDAO();

            if (request.getMethod().equals("GET")) {
                User user = dao.getUserById(userId);
                // 마이페이지
                request.setAttribute("user", user);
                return "/user/userinfo.jsp";
            } else {
                // POST든 패치든 일단 수정이니까 던짐 - 회원정보 수정
                request.setCharacterEncoding("UTF-8");

                String check_password = request.getParameter("check_password");
                User user = dao.getUserById(userId);

                // 유효성 검사
                if (check_password == null || check_password.trim().isEmpty() || !hashPassword(check_password).equals(user.getPassword())) {
                    request.setAttribute("error", "비밀번호를 입력하지 않았거나 틀립니다.");
                    return "/user/userinfo.jsp";
                }

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                // 중복 사용자명 확인
                UserDAO userDAO = new UserDAO();
                if (userDAO.getUserByUsername(username) != null && !username.equals(user.getUsername())) {
                    request.setAttribute("error", "이미 사용 중인 아이디입니다.");
                    return "/user/userinfo.jsp";
                }


                if (! (username == null || username.trim().isEmpty()) ) {
                    user.setUsername(username);
                }
                if (! (password == null || password.trim().isEmpty()) ) {
                    user.setPassword(hashPassword(password));
                }
                if (! (name == null || name.trim().isEmpty()) ) {
                    user.setName(username);
                }
                if (! (email == null || email.trim().isEmpty()) ) {
                    user.setEmail(username);
                }

                if (userDAO.insertUser(user)) {
//                  // 성공
//                  response.sendRedirect("front?command=login");
                    request.setAttribute("error", "회원 정보 수정 완료");
                    return "/user/userinfo.jsp";
                } else {
                    request.setAttribute("error", "회원 정보 수정 실패");
                    return "/user/userinfo.jsp";
                }
            }

        } catch (Exception e) {
            request.setAttribute("error", "회원 정보 수정 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "/user/userinfo.jsp";
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
