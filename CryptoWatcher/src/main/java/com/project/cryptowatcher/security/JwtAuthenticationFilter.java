package com.project.cryptowatcher.security;

import com.project.cryptowatcher.constants.ExceptionMessages;
import com.project.cryptowatcher.exception.UnauthorizedException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    private final ConcurrentMap<String, Bucket> bucketMap = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            if ("/auth/register".equals(requestURI) || "/auth/login".equals(requestURI)) {
                handleRateLimiting(request, response, filterChain);
                return;
            }

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                LOGGER.log(Level.WARNING, ExceptionMessages.MISSING_AUTH_BEARER);
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, ExceptionMessages.MISSING_AUTH_BEARER);
                return;
            }

            String token = authHeader.substring(7);
            validateTokenAndAuthenticate(token, response);

            filterChain.doFilter(request, response);

        } catch (UnauthorizedException exception) {
            throw exception;
        }
    }

    private void handleRateLimiting(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String clientIp = getClientIP(request);
        Bucket bucket = bucketMap.computeIfAbsent(clientIp, k -> createNewBucket());

        LOGGER.log(Level.INFO, "Available tokens for IP {0}: {1}", new Object[]{clientIp, bucket.getAvailableTokens()});

        if (!bucket.tryConsume(1)) {
            LOGGER.log(Level.WARNING, "Rate limit exceeded for IP: {0}", clientIp);
            sendErrorResponse(response, HttpStatus.TOO_MANY_REQUESTS, ExceptionMessages.TOO_MANY_REQUEST);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void validateTokenAndAuthenticate(String token, HttpServletResponse response) throws IOException {
        String username = jwtTokenUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.isTokenValid(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                LOGGER.log(Level.WARNING, "Invalid token for user: {0}", username);
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, ExceptionMessages.INVALID_TOKEN);
            }
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
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