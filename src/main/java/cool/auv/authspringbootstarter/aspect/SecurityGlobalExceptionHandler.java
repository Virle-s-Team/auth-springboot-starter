package cool.auv.authspringbootstarter.aspect;

import cool.auv.codegeneratorjpa.core.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.PrintWriter;

@RestControllerAdvice
@Slf4j
public class SecurityGlobalExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> appExceptionHandler(HttpServletResponse httpServletResponse, AppException e) {
        log.error("系统发生校验异常。", e);
        String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> appExceptionHandler(AuthenticationException e) {
        log.error("系统发生AuthenticationException异常。", e);
        String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        log.error("Spring Security AuthenticationException。", e);
        HttpResponseWriter.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        log.error("Spring Security AccessDeniedException。", e);
        HttpResponseWriter.sendError(response, HttpServletResponse.SC_FORBIDDEN, "AccessDenied");
    }

    private static class HttpResponseWriter {

        public static final String UTF8 = "UTF-8";

        public static final String APP_JSON = "application/json";

        public static final String TEXT = "text/plain";

        public static void sendError(HttpServletResponse httpServletResponse, Integer code, String msg) {
            httpServletResponse.setStatus(code);
            httpServletResponse.setCharacterEncoding(UTF8);
            httpServletResponse.setContentType(TEXT);
            try (PrintWriter printWriter = httpServletResponse.getWriter()) {
                printWriter.append(msg);
            } catch (IOException e) {
                // Nope
            }
        }

    }
}
