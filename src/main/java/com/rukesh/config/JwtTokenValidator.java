package com.rukesh.config;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenValidator.class);

    private static final String SECRET_KEY = JwtConstant.SECRET_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(JwtConstant.JWT_HEADER);

        if (header == null || !header.startsWith("Bearer ")) {
            logger.debug("Missing or invalid Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = header.substring(7); 

       
        if (jwt == null || jwt.split("\\.").length != 3) {
            logger.error("JWT token has invalid format: '{}'", jwt);
            throw new BadCredentialsException("Invalid JWT token format");
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            if (claims == null || claims.get("email") == null) {
                logger.error("JWT claims are invalid or missing");
                throw new BadCredentialsException("Invalid JWT claims");
            }

            String email = String.valueOf(claims.get("email"));
            String authorities = String.valueOf(claims.get("authorities"));

            List<GrantedAuthority> authList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authList);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException ex) {
            logger.error("JWT expired: {}", ex.getMessage());
            request.setAttribute("expired", ex.getMessage());
            throw new BadCredentialsException("Token expired", ex);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
            throw new BadCredentialsException("Invalid JWT token", ex);
        } catch (Exception ex) {
            logger.error("JWT token validation failed: {}", ex.getMessage());
            throw new BadCredentialsException("JWT validation failed", ex);
        }

        filterChain.doFilter(request, response);
    }

}
