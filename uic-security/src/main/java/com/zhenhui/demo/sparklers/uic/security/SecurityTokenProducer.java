package com.zhenhui.demo.sparklers.uic.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhenhui.demo.sparklers.uic.security.exception.ExpiresTokenException;
import com.zhenhui.demo.sparklers.uic.security.exception.InvalidTokenException;
import com.zhenhui.demo.sparklers.utils.JsonUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SecurityTokenProducer {

    private final String issuer;

    private final String expiresInSeconds;

    private final String secret;

    private SecretKey secretKey;

    public SecurityTokenProducer(String issuer, String expiresInSeconds, String secret) {
        this.issuer = issuer;
        this.expiresInSeconds = expiresInSeconds;
        this.secret = secret;
    }

    @PostConstruct
    public void init() {
        byte[] encodedKey = Base64.getDecoder().decode(secret);
        secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public String createToken(Subject subject) {
        return createToken(subject, Long.parseLong(expiresInSeconds), TimeUnit.SECONDS);
    }

    public String createToken(Subject subject, long expires, TimeUnit timeUnit) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        final Date now = new Date();
        JwtBuilder builder = Jwts.builder()
            .setIssuer(issuer)
            .setIssuedAt(now)
            .setSubject(JsonUtils.toJsonString(subject))
            .signWith(signatureAlgorithm, secretKey);

        if (expires >= 0) {
            builder.setExpiration(new Date(now.getTime() + timeUnit.toMillis(expires)));
        }

        return builder.compact();
    }

    public Subject parseToken(String token) throws ExpiresTokenException, InvalidTokenException {

        try {
            final String subject = Jwts.parser()
                .setSigningKey(secretKey)
                .requireIssuer(issuer)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

            return JsonUtils.fromJsonString(subject, new TypeReference<Subject>() {});
        }  catch (ExpiredJwtException e) {
            throw new ExpiresTokenException("expired token", e);
        } catch (Exception e) {
            throw new InvalidTokenException("invalid token", e);
        }

    }

}





