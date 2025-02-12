package org.example.bookstore.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.payload.request.*;
import org.example.bookstore.payload.response.*;
import org.example.bookstore.security.JwtTokenProvider;
import org.example.bookstore.service.Interface.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final String SIGN_UP_SUCCESS = "Sign Up Successful";
    private static final String SIGN_UP_FAILED = "Sign Up Failed";
    private static final String LOGIN_SUCCESS = "Login Successful";
    private static final String LOGIN_FAILED = "Login Failed";

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.createToken(authentication);
            DataResponse dataResponse = DataResponse.builder()
                    .code(HttpStatus.OK.value())
                    .message("Dang nhap thanh cong")
                    .data(new AuthenticationResponse(jwt))
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(dataResponse);
        } catch (AuthenticationException ex) {
            throw ex;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<DataResponse> register(@RequestBody RegisterRequest registerRequest) {
        boolean isSuccess = authenticationService.register(registerRequest);
        String message = isSuccess ? SIGN_UP_SUCCESS : SIGN_UP_FAILED;
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .data(isSuccess)
                .message(message)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/introspectToken")
    public ResponseEntity<IntrospectTokenResponse> introspect(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        IntrospectTokenResponse response = authenticationService.introspectToken(introspectRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logOut")
    public ResponseEntity<DataResponse> refreshToken(HttpServletRequest request) throws ParseException, JOSEException, ParseException, JOSEException {
        authenticationService.logOut(request);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Dang xuat thanh cong")
                .data("")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<DataResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(DataResponse.builder()
                .data(refreshTokenResponse)
                .timestamp(LocalDateTime.now())
                .build());
    }
}