package com.harshkumar0614jain.worksphere.filter;

import com.harshkumar0614jain.worksphere.service.JwtService;
import com.harshkumar0614jain.worksphere.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Read Authorization header from request
        String authHeader = request.getHeader("Authorization");

        // Step 2: If no token present → skip this filter
        // This handles /auth/login and /auth/register (public endpoints)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Extract token by removing "Bearer " prefix (7 characters)
        String token = authHeader.substring(7);

        // Step 4: Extract username from token
        String username = jwtService.extractUsername(token);

        // Step 5: If username found AND user is not already authenticated
        // SecurityContextHolder stores who is currently logged in
        // If it's null → no one is authenticated yet for this request
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 6: Load user details from MongoDB using username
            UserDetails userDetails =
                    myUserDetailsService.loadUserByUsername(username);

            // Step 7: Validate token
            if (jwtService.isTokenValid(token, userDetails)) {

                // Step 8: Create authentication object
                // null in middle = no credentials needed (token already verified)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Step 9: Attach request details to authentication
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Step 10: Register this user as authenticated in Spring Security
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // Step 11: Pass request to next filter or controller
        filterChain.doFilter(request, response);

    }
}
