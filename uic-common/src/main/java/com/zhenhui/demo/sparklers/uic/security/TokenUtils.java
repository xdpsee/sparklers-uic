package com.zhenhui.demo.sparklers.uic.security;

import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhenhui.demo.sparklers.uic.security.exception.ExpiresTokenException;
import com.zhenhui.demo.sparklers.uic.security.exception.InvalidTokenException;
import com.zhenhui.demo.sparklers.uic.utils.JSONUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils implements InitializingBean {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.ttl}")
    private String expiresInSeconds;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    @Override
    public void afterPropertiesSet() {
        byte[] encodedKey = Base64.getDecoder().decode(secret);
        secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }


    public String createToken(Principal principal) {
        return createToken(principal, Long.parseLong(expiresInSeconds), TimeUnit.SECONDS);
    }

    public String createToken(Principal principal, long expires, TimeUnit timeUnit) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        final Date now = new Date();
        JwtBuilder builder = Jwts.builder()
            .setIssuer(issuer)
            .setIssuedAt(now)
            .setSubject(JSONUtils.toJsonString(principal))
            .signWith(signatureAlgorithm, secretKey);

        if (expires >= 0) {
            builder.setExpiration(new Date(now.getTime() + timeUnit.toMillis(expires)));
        }

        return builder.compact();
    }

    public Principal parseToken(String token) throws ExpiresTokenException, InvalidTokenException {

        try {
            final String subject = Jwts.parser()
                .setSigningKey(secretKey)
                .requireIssuer(issuer)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

            return JSONUtils.fromJsonString(subject, new TypeReference<Principal>() {});
        }  catch (ExpiredJwtException e) {
            throw new ExpiresTokenException("expired token", e);
        } catch (Exception e) {
            throw new InvalidTokenException("invalid token", e);
        }

    }

}





