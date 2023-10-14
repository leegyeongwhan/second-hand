package com.secondhand.presentation.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LoggingException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUrl = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try {
            log.debug("REQUEST [{}][{}]", uuid, requestUrl);
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.debug("REQUEST [{}][{}]", uuid, requestUrl);
        }
    }

    @Override
    public void destroy() {
        log.debug("log filter destroy");
    }
}
