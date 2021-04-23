package com.idvsbruck.pplflw.api.security;

import java.util.Map;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class JwtOpenIdTokenConverter extends JwtAccessTokenConverter {

    public JwtOpenIdTokenConverter() {
        super();
        DefaultAccessTokenConverter tokenConverter = new JwtConverter();
        tokenConverter.setUserTokenConverter(new OpenIdUserAuthenticationConverter());
        setAccessTokenConverter(tokenConverter);
    }

    public static class JwtConverter extends DefaultAccessTokenConverter {

        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
            OAuth2Authentication authentication = super.extractAuthentication(map);
            authentication.setDetails(map);
            return authentication;
        }
    }

}
