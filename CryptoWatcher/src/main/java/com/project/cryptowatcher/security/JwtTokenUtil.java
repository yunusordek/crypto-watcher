package com.project.cryptowatcher.security;

import com.project.cryptowatcher.exception.InvalidTokenException;
import com.project.cryptowatcher.exception.TokenExpiredException;
import com.project.cryptowatcher.exception.TokenSignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtTokenUtil {

    private static final String SECRET_KEY = "256-bit-secret-key-256-bit-secret-key";
    private static final long EXPIRATION_TIME = 86400000;
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * Kullanıcı adı üzerinden JWT token oluşturur.
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
     * Token'ın süresinin dolup dolmadığını kontrol eder.
     *
     * @param token JWT token
     * @return Süresi dolmuş mu?
     */
    private boolean isTokenExpired(String token) {
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
            throw new TokenSignatureException("Token signature is invalid", e);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Token has expired", e);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token", e);
        }
    }
}
