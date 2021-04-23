package com.idvsbruck.pplflw.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

public class OpenIdUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    private final static String USERNAME = "sub";

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map != null && map.containsKey(USERNAME)) {
            Object principal = map.get(USERNAME);
            Collection<? extends GrantedAuthority> authorities = getAuthoritiesNew(map);
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesNew(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities == null) {
            return new HashSet<>();
        }
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.createAuthorityList((String[]) ((Collection) authorities).toArray(new String[0]));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
