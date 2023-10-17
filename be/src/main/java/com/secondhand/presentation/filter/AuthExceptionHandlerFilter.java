package com.secondhand.presentation.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.ErrorResponse;
import com.secondhand.exception.v2.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AuthExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UnAuthorizedException e) {
            setErrorResponse(response, e.getErrorMessage());
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorMessage errorCode) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
