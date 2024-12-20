package com.project.cryptowatcher.security;

import com.project.cryptowatcher.constants.ExceptionMessages;
import com.project.cryptowatcher.exception.InvalidTokenException;
import com.project.cryptowatcher.exception.TokenExpiredException;
import com.project.cryptowatcher.exception.TokenSignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenUtil {

    private static final long EXPIRATION_TIME = 86400000; // 1 gün
    private static final long REFRESH_EXPIRATION_TIME = 2592000000L; // 30 gün
    private final SecretKey secretKey;

    /**
     * Kullanıcı adı üzerinden JWT token oluşturur.
     * Access Token: Kullanıcı isteği yaparken kimlik doğrulaması için kullanılır.
     * Genellikle kısa süreli geçerliliğe sahiptir (örneğin, 1 gün).
     *
     * @param username Kullanıcı adı
     * @return Oluşturulan JWT token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Kullanıcı adı üzerinden JWT refresh token oluşturur.
     * Refresh Token: Access token süresi dolduğunda yeni bir access token almak için kullanılır.
     * Daha uzun süreli geçerliliğe sahiptir (örneğin, 30 gün).
     *
     * @param username Kullanıcı adı
     * @return Oluşturulan JWT refresh token
     */
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Token'dan kullanıcı adını çıkartır.
     *
     * @param token JWT token
     * @return Kullanıcı adı
     */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Token geçerliliğini kontrol eder.
     *
     * @param token    JWT token
     * @param username Kullanıcı adı
     * @return Token geçerli mi?
     */
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    /**
     * Refresh Token geçerliliğini kontrol eder.
     *
     * @param token JWT refresh token
     * @return Token geçerli mi?
     */
    public boolean isRefreshTokenValid(String token) {
        return !isTokenExpired(token);
    }

    /**
     * Token'ın süresinin dolup dolmadığını kontrol eder.
     *
     * @param token JWT token
     * @return Süresi dolmuş mu?
     */
    public boolean isTokenExpired(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Token'ı parse edip Claims nesnesini döndürür.
     *
     * @param token JWT token
     * @return Claims nesnesi
     */
    private Claims parseClaims(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return jws.getPayload();
        } catch (SignatureException e) {
            throw new TokenSignatureException(ExceptionMessages.TOKEN_SIGNATURE_INVALID);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(ExceptionMessages.TOKEN_EXPIRED, e);
        } catch (Exception e) {
            throw new InvalidTokenException(ExceptionMessages.INVALID_TOKEN, e);
        }
    }

    /**
     * Refresh Token oluşturur.
     * Access Token Yenileme: refreshToken
     *
     * @param oldToken Eski JWT token
     * @return Yeni JWT token
     */
    public String refreshToken(String oldToken) {
        if (isTokenExpired(oldToken)) {
            throw new TokenExpiredException(ExceptionMessages.TOKEN_EXPIRED);
        }

        String username = extractUsername(oldToken);
        return generateToken(username);
    }
}