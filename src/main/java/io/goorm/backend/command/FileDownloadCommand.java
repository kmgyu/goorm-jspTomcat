package io.goorm.backend.command;

import io.goorm.backend.FileUpload;
import io.goorm.backend.FileUploadDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class FileDownloadCommand implements Command{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileIdStr = request.getParameter("id");
            if (fileIdStr == null || fileIdStr.trim().isEmpty()) {
                throw new ServletException("파일 ID가 필요합니다.");
            }

            Long fileId = Long.parseLong(fileIdStr);
            FileUploadDAO fileDAO = new FileUploadDAO();
            FileUpload fileUpload = fileDAO.getFileById(fileId);

            if (fileUpload == null) {
                throw new ServletException("파일을 찾을 수 없습니다.");
            }

            // 파일 경로 검증
            String filePath = validateAndGetFilePath(fileUpload.getFilePath());
            File file = new File(filePath);
            if (!file.exists()) {
                throw new ServletException("물리적 파일이 존재하지 않습니다.");
            }

            // 다운로드 헤더 설정
            setDownloadHeaders(response, fileUpload);

            // 파일 스트림 전송
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }

            return null; // 파일 다운로드는 직접 스트림으로 처리
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "파일 다운로드 중 오류가 발생했습니다: " + e.getMessage());
            return "/board/view.jsp";
        }
    }
}
