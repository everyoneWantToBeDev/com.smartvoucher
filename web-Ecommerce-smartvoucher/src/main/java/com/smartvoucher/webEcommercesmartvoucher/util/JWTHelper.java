package com.smartvoucher.webEcommercesmartvoucher.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTHelper {

    @Value("${jwt.token.key}")
    private String jwtKey;
    @Value("${access.token.expired}")
    private Long accessTokenExpired;
    @Value("${refresh.token.expired}")
    private Long refreshTokenExpired;

    private SecretKey getKeys(){
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtKey));
    }

    public String generateToken(String data){
        long expiredTime = System.currentTimeMillis() + accessTokenExpired;
        Date newExpiredTime = new Date(expiredTime);
        return Jwts.builder()
                .setSubject(data)
                .setIssuer("com.smartvoucher")
                .signWith(getKeys())
                .setExpiration(newExpiredTime)
                .compact();
    }

    public String generateRefreshToken(String data){
        long expiredTime = System.currentTimeMillis() + refreshTokenExpired;
        Date newExpiredTime = new Date(expiredTime);
        return Jwts.builder()
                .setSubject(data)
                .signWith(getKeys())
                .setExpiration(newExpiredTime)
                .compact();
    }

    public String parserToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKeys()).build()
                .parseClaimsJws(token).getBody()
                .getSubject();
    }
}