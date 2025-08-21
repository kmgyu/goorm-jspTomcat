package io.goorm.backend.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@WebFilter("/*")
public class XSSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // XSS 방지 래퍼로 요청 감싸기
        XSSRequestWrapper wrappedRequest = new XSSRequestWrapper((HttpServletRequest) request);

        // 다음 필터 또는 서블릿으로 전달
        chain.doFilter(wrappedRequest, response);
    }

    @Override
    public void destroy() {
        // 필터 소멸
    }

    // XSS 방지 래퍼 클래스
    private static class XSSRequestWrapper extends HttpServletRequestWrapper {

        public XSSRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            return stripXSS(value);
        }

        @Override
        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);
            if (values == null) return null;

            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = stripXSS(values[i]);
            }
            return encodedValues;
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return stripXSS(value);
        }

        // XSS 패턴 제거
        private String stripXSS(String value) {
            if (value == null) return null;

            // 악성 스크립트 패턴 제거
            value = value.replaceAll("", "");

            // HTML 태그 제거
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

            // JavaScript 이벤트 제거
            value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
            value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
            value = value.replaceAll("(?i)<.*?\\s+on.*?=.*?>", "");

            // 위험한 속성 제거
            value = value.replaceAll("(?i)<.*?\\s+on.*?=.*?>", "");
            value = value.replaceAll("(?i)<.*?\\s+on.*?=.*?>", "");

            return value;
        }
    }
}