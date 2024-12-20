package com.project.cryptowatcher.security;

import com.project.cryptowatcher.constants.ExceptionMessages;
import com.project.cryptowatcher.exception.InvalidTokenException;
import com.project.cryptowatcher.exception.RateLimitException;
import com.project.cryptowatcher.exception.UnauthorizedException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(JwtAuthenticationFilter.class.getName());
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;
    private final ConcurrentMap<String, Bucket> bucketMap = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String requestURI = request.getRequestURI();
            if (isAuthEndpoint(requestURI)) {
                handleRateLimiting(request, response, filterChain);
                return;
            }

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                LOGGER.log(Level.WARNING, ExceptionMessages.MISSING_AUTH_BEARER);
                throw new UnauthorizedException(ExceptionMessages.MISSING_AUTH_BEARER);
            }

            String token = authHeader.substring(7);
            validateTokenAndAuthenticate(token, response);

            filterChain.doFilter(request, response);

        } catch (UnauthorizedException | RateLimitException | InvalidTokenException exception) {
            sendErrorResponse(response, exception);
        } catch (Exception exception) {
            sendErrorResponse(response, new RuntimeException(exception.getMessage()));
        }
    }

    private boolean isAuthEndpoint(String requestURI) {
        return "/auth/register".equals(requestURI) || "/auth/login".equals(requestURI);
    }

    private void handleRateLimiting(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String clientIp = getClientIP(request);
        Bucket bucket = bucketMap.computeIfAbsent(clientIp, k -> createNewBucket());

        LOGGER.log(Level.INFO, "Available tokens for IP {0}: {1}", new Object[]{clientIp, bucket.getAvailableTokens()});

        if (!bucket.tryConsume(1)) {
            LOGGER.log(Level.WARNING, "Rate limit exceeded for IP: {0}", clientIp);
            throw new RateLimitException(ExceptionMessages.TOO_MANY_REQUEST);
        }

        filterChain.doFilter(request, response);
    }

    private void validateTokenAndAuthenticate(String token, HttpServletResponse response) throws IOException {
        String username = jwtTokenUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.isTokenValid(token, userDetails.getUsername()) && redisTemplate.hasKey(token)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                handleTokenRefresh(token, response, username);
            }
        }
    }

    private void handleTokenRefresh(String token, HttpServletResponse response, String username) throws IOException {
        try {
            if (jwtTokenUtil.isRefreshTokenValid(token)) {
                String newToken = jwtTokenUtil.refreshToken(token);
                response.setHeader("Authorization", "Bearer " + newToken);
            } else {
                LOGGER.log(Level.WARNING, "Invalid token for user: {0}", username);
                throw new InvalidTokenException(ExceptionMessages.INVALID_TOKEN);
            }
        } catch (UnauthorizedException e) {
            LOGGER.log(Level.WARNING, "Refresh token expired for user: " + username);
            throw new UnauthorizedException(ExceptionMessages.TOKEN_EXPIRED);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, Exception exception) throws IOException {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception instanceof UnauthorizedException) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof RateLimitException) {
            status = HttpStatus.TOO_MANY_REQUESTS;
        } else if (exception instanceof InvalidTokenException) {
            status = HttpStatus.UNAUTHORIZED;
        }
        response.setStatus(status.value());
        response.getOutputStream().write(exception.getMessage().getBytes(StandardCharsets.UTF_8));
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return normalizeIpAddress(request.getRemoteAddr());
        }
        return xfHeader.split(",")[0];
    }

    private String normalizeIpAddress(String ip) {
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}