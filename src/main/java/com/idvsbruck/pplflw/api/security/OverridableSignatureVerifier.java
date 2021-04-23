package com.idvsbruck.pplflw.api.security;

import lombok.Getter;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

public class OverridableSignatureVerifier implements SignatureVerifier {

    @Getter
    private final SignatureVerifier realVerifier;

    public OverridableSignatureVerifier(SignatureVerifier realVerifier) {
        this.realVerifier = realVerifier;
    }

    @Override
    public void verify(byte[] content, byte[] signature) {
        realVerifier.verify(content, signature);
    }

    @Override
    public String algorithm() {
        return realVerifier.algorithm();
    }
}
