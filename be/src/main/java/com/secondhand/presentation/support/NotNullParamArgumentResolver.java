package com.secondhand.presentation.support;

import com.secondhand.exception.BadRequestException;
import com.secondhand.exception.v2.ErrorMessage;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class NotNullParamArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(NotNullParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        NotNullParam annotation = parameter.getParameterAnnotation(NotNullParam.class);
        String queryParamKey = annotation.key();
        if (!StringUtils.hasText(queryParamKey)) {
            queryParamKey = parameter.getParameterName();
        }
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        return Optional.ofNullable(request.getParameter(queryParamKey))
                .orElseThrow(() -> new BadRequestException(ErrorMessage.INVALID_PARAMETER, annotation.message()));
    }
}
