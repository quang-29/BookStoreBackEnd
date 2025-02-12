package org.example.bookstore.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.payload.response.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String errorMessage;

        if (authException instanceof BadCredentialsException) {
            errorMessage = "Ten dang nhap hoac mat khau khong chinh xac";
        } else {
            errorMessage = "Xac thuc khong thanh cong: " + authException.getLocalizedMessage();
        }

        // Tạo response JSON
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .data("")
                .message(errorMessage)
                .status(HttpStatus.UNAUTHORIZED)
                .build();

        // Ghi response vào output stream
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(dataResponse));
        response.flushBuffer();
    }
}