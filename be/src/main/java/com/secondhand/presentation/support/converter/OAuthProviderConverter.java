package com.secondhand.presentation.support.converter;

import com.secondhand.domain.oauth.OAuthProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OAuthProviderConverter implements Converter<String, OAuthProvider> {

    @Override
    public OAuthProvider convert(String oAuthProvider) {
        return OAuthProvider.of(oAuthProvider);
    }
}
