package com.project.fashionshops.filters;

import com.project.fashionshops.components.JwtTokenUtil;
import com.project.fashionshops.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// filters no la bộ lọc
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (isBypassToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        //can kiem tra
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")){
final String token = authHeader.substring(7);
final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
if (phoneNumber != null
        && SecurityContextHolder.getContext()
        .getAuthentication() == null
){
 UserDetails userDetails = userDetailsService
         .loadUserByUsername(phoneNumber);
 if (jwtTokenUtil.validateToken(token,userDetails)){
     // tao ra 1 doi tuong authen
     UsernamePasswordAuthenticationToken authenticationToken =
             new UsernamePasswordAuthenticationToken(userDetails, null,
                     userDetails.getAuthorities()
             );
     authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
     SecurityContextHolder.getContext().setAuthentication(authenticationToken);
 }
}
        }
filterChain.doFilter(request,response);
    }

    //filterChain.doFilter(request,response); // cho di qua heet

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products",apiPrefix), "GET"), // noi %s chuoi api vao
                Pair.of(String.format("%s/categories",apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register",apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login",apiPrefix), "POST")

        );
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getLeft()) &&
                    request.getMethod().equals(bypassToken.getRight())) {
                return true;
            }
        }
        return false;
        // mỗi request đi qua OncePerRequestFilters để kiêm tra từ 2 cái đăng nhập đăng kí'
    }
}
