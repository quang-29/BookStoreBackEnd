package org.example.bookstore.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-in-ms}")
    private int jwtExpirationInMs;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private Key key;
    @Autowired
    private UserRepository userRepository;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String jwtSecret) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }


    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);


        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long tokenId = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", tokenId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .claim("scope", authorities)
                .setHeaderParam("typ", "JWT")
                .claim("scope", buildScope(user))
                .compact();
    }

    // Trích xuất token từ request
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Xác thực token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        }
        return false;
    }

    // Lấy username từ token
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public UUID getTokenId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id", UUID.class);
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }
    public String refreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Lấy thông tin username và quyền hạn từ token cũ
            String username = claims.getSubject();
            String authorities = claims.get("scope", String.class);
            long tokenId = System.currentTimeMillis();


            // Kiểm tra nếu token đã hết hạn
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

            // Tạo token mới
            return Jwts.builder()
                    .setSubject(username)
                    .claim("id", tokenId)
                    .claim("scope", authorities)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setHeaderParam("typ", "JWT")
                    .compact();
        } catch (ExpiredJwtException ex) {
            Claims claims = ex.getClaims();
            String username = claims.getSubject();
            String authorities = claims.get("scope", String.class);

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
            return Jwts.builder()
                    .setSubject(username)
                    .claim("scope", authorities)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setHeaderParam("typ", "JWT")
                    .compact();
        } catch (Exception ex) {
            log.error("Cannot refresh token: {}", ex.getMessage());
            return null;
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add(role.getRoleName())); // Lấy roleName thay vì toàn bộ đối tượng Role
        }

        return stringJoiner.toString();
    }


}