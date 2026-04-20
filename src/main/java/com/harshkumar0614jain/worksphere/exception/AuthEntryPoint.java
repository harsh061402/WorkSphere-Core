package com.harshkumar0614jain.worksphere.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harshkumar0614jain.worksphere.model.ExceptionResponseModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(@NonNull HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String,String> error = new HashMap<>();
        error.put("error",authException.getMessage());
        ExceptionResponseModel body = new ExceptionResponseModel(
                "Unauthorised or missing token", error);

        response.getWriter().write(
                new ObjectMapper().writeValueAsString(body));
    }
}
