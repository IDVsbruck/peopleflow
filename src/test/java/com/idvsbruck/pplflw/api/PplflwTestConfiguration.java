package com.idvsbruck.pplflw.api;

import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.MockRestServiceServer.bindTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.SimpleRequestExpectationManager;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class PplflwTestConfiguration {

    private final static String MOCK_PRIVATE_KEY_STR = "MIIJQgIBADANBgkqhkiG9w0BAQEFAASCCSwwggkoAgEAAoICAQCJi9ujELWj9Qx"
            + "Tao6bChiX+7BJ1j6IGFu8D84nzh+Mp0mGfMPURPbQXGhpTRt7INeaU8tJhthZ4aBSBQzvi3SRVzP0GneqqzC8CMkwfSNLDkfJ6lpGTeb"
            + "2n0BcZ/ODjGxT8zST1NGwRZUyzy6+8p22AzBj5vlwa3czlL5EzgrHeYKrjgdNh805kh0/+i02a5GiglXN8qJPMoxDJnvPMpdgNSD5UGx"
            + "ULoCYgicvxzloD6PpWCyH/+KNabVAsnaDtvvORNx66UweWQFVNp4giKnb34tslsvXW1Ouu+QhpFOzxRk59DLClaOFQALvPfSpp8+Q7Z6"
            + "j6Xq2yGQtMh/7Llfj+RTJmAUI+mMIvdo6akRoOZbESlryPoQW0DTeKy9kgrQWz9ZDCVtasd8gDm4GyQlO8jbJ29EEQT/neDYIMCLpOwm"
            + "BOJVaMm8/gjK/mFvfirsQ6/eksKbJ0CMANSRd65oeHI0oSQgr0DifbQhRqGDM06v84w20WimJbFLVtF9P+uq1gvm0JIgxKWwoUuQive2"
            + "ICSuUmJFoAlH+Ja4n0Ek9vG32woFdVQQ7lJZUC4RnK+Y4+3jMPBE+z5vjW94r9aSCKgOYaZ+2YiUs7lrgm+w3bXe2V+yW3auWfatKDsQ"
            + "8DLH3Q2BS/T47HUcgjQSMva3Vtdd/JQbKXovkpJBklEA+fwIDAQABAoICAHz//EgSPHDXly8LzLO7liQxGMHRkZyPPncHihwEqAlkUl6"
            + "FblavofozsLPZ3lqkuyvGcR3ODTqJ4PAJJPthqjsXm+CAWTZiQ3TvKyAE6ZkhTj6C2y2/SGHi0lPoKJbpe91DTgn3Q+VFJ1U4kkv4Izm"
            + "xZj20QAZZs8fNqqjO817a39TWy247N1fVoP1ud75YPc8JUb9LfRQqZOv1wljHqmhFgETzQK+0Xyu6RLCYBmS6qgS7HCUweAx5/73fMfN"
            + "7zRVk4VZWRNXjn2F4tHXunSdz3bp+xJtfpQpMOQQV2feq7MUNV0AdS7EiPkh77qhsGCemuyBNZDdOoDOutP/J2xa+3Faf2iYMaxVNyY4"
            + "5qG2CUhlugqTnZYJuMiqAE/XkfMyQksWmpRLubm2Yd2LzOOJDEWxsEFPHIH9jyZeB/bXIlpTHX5q5CaaH/aBJkSn5lprgNu5zabuxlpP"
            + "WXMXROg1z/ZPY4G4auTxK0QcQ63iCw05EFmH5mheP5bTf2T3P/JLlpPcWJW54mNmb8yC/FhXWdyl5iU2J9hH03BJWyeucj5tmSSkFR+N"
            + "to+FiSHkrqmxLcrwu1g2zjRaFXtoBrPwxAg5A/4UqofvcCXA8Tp3yWzuyuQyEwvmHgEb3DTTHfqbokr/0lNDAz+J2cnvEmR/6tctgONo"
            + "L1vcvHnVGkEVRAoIBAQDFSmbHkeAz1dPkFXchGn0a6UyGXJSrWE9G/f5lUJCdLuR1iYVIbbtPaR/LzvWlfiB2oxA3MnAbKP5dkx7bj7E"
            + "rJsFQdWenaN0dje0iVuvwUKXwFuUpmgRNHg52657TdUtgpEVvK8Y+NEYeFgyp6LHDofYvQgDutJccTqicT33E43ESU6EO1xzO36oI9C+"
            + "hbN5SAXiMXtNGAmSgTi1hMquKxg6MGgkQOsn1gNQGWDCSKqXvVW+SiVIT7uhTOIdHbHEVZjQbkKuxeVpnKZjQp+XAwt/5uhP6gEZDh8D"
            + "hQQ0vqlS4CIdEccWuNfL8fuHTZb1mdPpvKv4NJGaDUZOO7XhHAoIBAQCyeiKqjZXc604csW76lIXsNqOk1uoRTlIsXXcWeUOocGbIAun"
            + "SZt2LOnc5Ph0W9YbqUZlA6e+78EImXLK37sTBdC8XCeSE9nlbIZCi+0LO6ptwXsL7Ww4ljXE5sfPJYyzTSa3oTC6BNZbzEdhTXtAatDT"
            + "/R2iltflkT8Df1Nvsk/0RBqUKTJIXLkmY6iX3qJx93mMsF3RRJf9IVuqrJG9/EQwFkrBOMATLu6P8weBv4MWOivo1Sa8DBf4w5ybD3Fw"
            + "RZye/G7VAwtVIfy9qVmRik+3+5KJnLNnVdQziNMmwHqiyzWyJFsdKn6w0/sh2Bu5NzdZC3KtlB7KCEcpgJtwJAoIBAAmAL7rkl3tnjLC"
            + "rJ/V8JRIqsfi2dKJbulWc3adbXdtz6qSOXtDCGAcW9OUHrmSt0jpkV9+Qmj10l+tBrna8ULfXQe/x92/kaOGHeCfzL6F/AL6zG44JULO"
            + "2AtRPPHLKbzrULlPQM9fDBK1mOm3kOstE/WoBL7JPGAfQ8eW1HkVg/oz3YgYo7cY4lyOfPrvzVjF0yK1Z06rHarkdiqnnmsMwDntIta8"
            + "GZbtg3NUBYjVnwF3qK1lPK5iyJJX9XuZdnoR9S30YVmxRf70AD8/chf/mYorQHy4tBzUxUGSIkW3+Md466uis4ewlxPHL2mwnths8/uJ"
            + "jm6BeZGFmiEiVvvkCggEAVERkX8CP1InpDJUeAAPmI3w80ZSDWX5wP/A1TRAeSMYhUShG/AeDbLxDFGzmUTPF6pZyVHrfrQ2oPfKgk0W"
            + "R8oEHxsnt8nVpIQT9BGa7yXRtxaWITCNWz5Yzsnj50MkZnfz4tmhZwLnrtoJjcCGhAiq5pxoxJ6R+xsT9HPGkkNPitYo8nFtA0t8Q8rk"
            + "DCia7FJbOnj/ItJPLL32SORHv7r++vFbhFVmIuiSzLaDgdhJbVIz7y/MpRbUrqi1JWUqO0cyxsILFlnknOJ6MZZm6teyAf0u1/h7oDuf"
            + "AGIGyBIzFO/7P0v5FRE+VgEQXo9pr46JfGpyT9BSpTM5XjgRq+QKCAQEAnEmbWou2KBCrxfFD1rzUmAT559/X7QKj2xxuxnd1L7PTkfT"
            + "x1rXgtRNrP2XHj6eIP6ak9Z9d+rrisM0RaBghYGNbEbnDUveavc8lHR6Mtq0gV4VCNX1ZD5QcX5JSd59rKBlXmEDHfYIEaPmXwa7kUIQ"
            + "noyy8VhpLiY3R7U17xN/X65/mgKdjvs981dQvkiSVylHTC29mpM0ewr4ZWZRl7gx40wBk4mKGmWpCxMQt8llFEVoq+n1RlGaFMTJcx6h"
            + "ROYkhvUUj6/Z1OJnZHdoI1IM4bA6zjXEf0qyNDIbIcS84E7+k9jFaXQt4rpzhd591hgbukqvZa6MgicFJEQjxAw==";
    private final static int MOCK_EXPIRATION = 3600000;
    private final static String MOCK_ISSUER = "http://localhost:999";
    private final static String CLIENT_ID = "com.idvsbruck.pplflw.api";

    @Value("${security.oauth2.client.jwtKeyUri}")
    private String jwtKeyUri;

    @Value(value = "${security.oauth2.client.scopes}")
    private String clientScopes;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() throws Exception {
        mockAuthorizationServer();
    }

    @Bean
    public MockRestServiceServer mockAuthorizationServer() throws Exception {
        var mockServer = bindTo(restTemplate).bufferContent().build(new SimpleRequestExpectationManager());
        mockServer.expect(manyTimes(), requestTo(jwtKeyUri)).andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).body(getPublicKey()));
        ((OAuth2RestTemplate) restTemplate).getOAuth2ClientContext()
                .setAccessToken(new DefaultOAuth2AccessToken(getClientToken()));
        return mockServer;
    }

    public static String getToken(final String email, final List<String> scopes) throws Exception {
        var now = System.currentTimeMillis();
        return Jwts.builder().setId(UUID.randomUUID().toString()).setIssuedAt(new Date(now)).setSubject(email)
                .setIssuer(MOCK_ISSUER).setExpiration(new Date(now + MOCK_EXPIRATION)).claim("user_name", email)
                .claim("azp", CLIENT_ID).claim("scope", scopes).claim("authorities", List.of("USER"))
                .claim("client_id", CLIENT_ID).setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS256, getPrivateKey()).compact();
    }

    private String getClientToken() throws Exception {
        var now = System.currentTimeMillis();
        return Jwts.builder().setId(UUID.randomUUID().toString()).setIssuedAt(new Date(now)).setIssuer(MOCK_ISSUER)
                .setExpiration(new Date(now + MOCK_EXPIRATION)).setAudience(CLIENT_ID).claim("azp", CLIENT_ID)
                .claim("scope", clientScopes).claim("client_id", CLIENT_ID)
                .setHeaderParam("typ", "JWT").signWith(SignatureAlgorithm.RS256, getPrivateKey()).compact();
    }

    private String getPublicKey() throws Exception {
        var privateKeySpec = KeyFactory.getInstance("RSA").getKeySpec(getPrivateKey(), RSAPrivateKeySpec.class);
        var modulus = Base64Utils.encodeToString(privateKeySpec.getModulus().toByteArray());
        var exponent = Base64Utils.encodeToString(BigInteger.valueOf(65537).toByteArray());
        return modulus + "|" + exponent;
    }

    private static PrivateKey getPrivateKey() throws Exception {
        var keySpec = new PKCS8EncodedKeySpec(Base64Utils.decode(MOCK_PRIVATE_KEY_STR.getBytes()));
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }
}
