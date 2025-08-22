package io.goorm.backend.command;

import io.goorm.backend.FileUpload;
import io.goorm.backend.FileUploadDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadCommand implements Command{
    // 업로드 루트 디렉터리: 실제 서버 경로로 맞춰주세요.
    private static final Path BASE_UPLOAD_DIR = Paths.get("/var/app/uploads").toAbsolutePath().normalize();

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

    /**
     * 업로드 경로를 검증하고 정규화된 절대 경로를 반환.
     * - 널/공백, NUL 바이트, 상위 경로(..) 차단
     * - 업로드 루트(BASE_UPLOAD_DIR) 밖으로 이탈 금지
     * - 심볼릭 링크 우회 방지
     */
    private String validateAndGetFilePath(String rawPath) throws ServletException {
        if (rawPath == null || rawPath.trim().isEmpty()) {
            throw new ServletException("파일 경로가 비어 있습니다.");
        }
        if (rawPath.indexOf('\0') >= 0) {
            throw new ServletException("유효하지 않은 파일 경로입니다.");
        }

        try {
            // DB에는 상대 경로를 저장했다고 가정
            Path candidate = BASE_UPLOAD_DIR.resolve(rawPath).normalize();

            if (!candidate.startsWith(BASE_UPLOAD_DIR)) {
                throw new ServletException("허용되지 않은 파일 경로 접근입니다.");
            }

            Path baseReal = BASE_UPLOAD_DIR.toRealPath(LinkOption.NOFOLLOW_LINKS);
            Path candReal = candidate.toRealPath(LinkOption.NOFOLLOW_LINKS);

            if (!candReal.startsWith(baseReal)) {
                throw new ServletException("허용되지 않은 파일 경로 접근입니다.");
            }

            return candReal.toString();
        } catch (IOException e) {
            throw new ServletException("파일 경로 검증 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 다운로드 응답 헤더 설정.
     * - Content-Type: DB 값 우선, 없으면 probeContentType, 그래도 없으면 application/octet-stream
     * - Content-Disposition: filename + RFC 5987 filename* (UTF-8)
     * - Content-Length, nosniff, 캐시 무효화 헤더
     */
    private void setDownloadHeaders(HttpServletResponse response, FileUpload fileUpload) throws ServletException {
        try {
            String storedPath = validateAndGetFilePath(fileUpload.getFilePath());
            Path path = Paths.get(storedPath);

            long size = Files.size(path);

            String downloadName = fileUpload.getOriginalFilename();
            if (downloadName == null || downloadName.isBlank()) {
                downloadName = path.getFileName().toString();
            }

            String contentType = fileUpload.getContentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = Files.probeContentType(path);
            }
            if (contentType == null || contentType.isBlank()) {
                contentType = "application/octet-stream";
            }

            // filename 및 RFC 5987 filename*
            String asciiFallback = downloadName.replaceAll("[\\r\\n\"]", "_");
            String quoted = asciiFallback.replace("\\", "\\\\").replace("\"", "\\\"");
            String encodedUTF8 = URLEncoder.encode(downloadName, StandardCharsets.UTF_8).replace("+", "%20");

            response.setContentType(contentType);
            response.setHeader("Content-Length", String.valueOf(size));
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=\"" + quoted + "\"; filename*=UTF-8''" + encodedUTF8
            );

            response.setHeader("X-Content-Type-Options", "nosniff");
            response.setHeader("Cache-Control", "private, max-age=0, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);

        } catch (IOException e) {
            throw new ServletException("다운로드 헤더 설정 중 오류가 발생했습니다.", e);
        }
    }
}
