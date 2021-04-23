package com.idvsbruck.pplflw.api.config;

import com.idvsbruck.pplflw.api.handlers.RestTemplateResponseErrorHandler;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableOAuth2Client
@RequiredArgsConstructor
public class OAuth2ClientConfiguration {

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    @Value("#{'${security.oauth2.client.scopes}'.split(' ')}")
    private List<String> scopes;

    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;

    @Value("${http.client.connectTimeout}")
    private int httpClientConnectTimeout;

    @Value("${http.client.connectionRequestTimeout}")
    private int httpClientConnectionRequestTimeout;

    @Value("${http.client.readTimeout}")
    private int httpClientReadTimeout;

    private final RestTemplateResponseErrorHandler errorHandler;

    @Bean(name = "oauthClientCredentialsRestTemplate")
    public RestTemplate oauthRestTemplate() {
        var resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setClientId(clientId);
        resourceDetails.setClientSecret(clientSecret);
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setScope(scopes);
        resourceDetails.setAuthenticationScheme(AuthenticationScheme.header);
        resourceDetails.setGrantType("client_credentials");
        var restTemplate = new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext());
        final var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(httpClientConnectTimeout);
        requestFactory.setConnectionRequestTimeout(httpClientConnectionRequestTimeout);
        requestFactory.setReadTimeout(httpClientReadTimeout);
        restTemplate.setRequestFactory(requestFactory);
        var accessTokenProvider = new ClientCredentialsAccessTokenProvider();
        restTemplate.setAccessTokenProvider(new AccessTokenProviderChain(Arrays.<AccessTokenProvider>asList(
                new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
                new ResourceOwnerPasswordAccessTokenProvider(), accessTokenProvider)));
        restTemplate.setErrorHandler(errorHandler);
        return restTemplate;
    }
}
