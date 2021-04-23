package com.idvsbruck.pplflw.api.security;

import com.nimbusds.jose.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableResourceServer
@RequiredArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter implements InitializingBean {

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.jwtKeyUri}")
    private String jwtKeyUri;

    private final RestTemplate restTemplate;

    private SignatureVerifier jwtPublicSignatureVerifier;

    @Override
    public void afterPropertiesSet() {
        final String publicKeyStr = restTemplate.getForObject(jwtKeyUri, String.class);
        String[] keyParts = Objects.requireNonNull(publicKeyStr).split("\\|");
        final SignatureVerifier signatureVerifier = new RsaVerifier(new Base64(keyParts[0]).decodeToBigInteger(),
                new Base64(keyParts[1]).decodeToBigInteger());
        jwtPublicSignatureVerifier = new OverridableSignatureVerifier(signatureVerifier);
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.resourceId(clientId).tokenServices(tokenServices());
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .headers().cacheControl().disable().and()
                .anonymous().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyRequest().authenticated();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtOpenIdTokenConverter converter = new JwtOpenIdTokenConverter();
        converter.setVerifier(jwtPublicSignatureVerifier);
        return converter;
    }

    @Bean
    @Primary
    public ResourceServerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new ResourceTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }
}
