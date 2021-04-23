package com.idvsbruck.pplflw.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
@RequiredArgsConstructor
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper mapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    @SuppressWarnings("unused")
    public void handleError(ClientHttpResponse response) throws IOException {
        var json = new String(new BufferedInputStream(response.getBody()).readAllBytes(), StandardCharsets.UTF_8);
        var errorResponse = mapper.readValue(json, Object.class);
        //TODO: handle response of OAuth2 Security Server object (instead Object) and throw custom excption
    }
}
