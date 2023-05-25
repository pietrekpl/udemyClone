package com.example.corneradmin.jwt.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.corneradmin.jwt.JWTHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.example.corneradmin.constants.JWTUtil.AUTH_HEADER;
import static com.example.corneradmin.constants.JWTUtil.SECRET;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {


    private final JWTHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/refresh-token")) {
            filterChain.doFilter(request, response);
        } else {
            String accessToken = jwtHelper.extractTokenFromHeaderIfExists(request.getHeader(AUTH_HEADER));
            if (accessToken != null) {
                Algorithm algorithm = Algorithm.HMAC256(SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);
                String email = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                log.info("ROLES LENGTH{}", roles.length );
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

}
