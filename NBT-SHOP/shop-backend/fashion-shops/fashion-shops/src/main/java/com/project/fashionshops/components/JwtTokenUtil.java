package com.project.fashionshops.components;

import com.project.fashionshops.exceptions.InvalidParamException;
import com.project.fashionshops.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.*;
import java.util.HashMap;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration; // luu vao bien moi truong

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(com.project.fashionshops.models.User user) throws Exception{
        //import java.util.*;
        Map<String, Object> claims = new HashMap<>();
       //this.generateSecretKey(); chi lay token 1 lan
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
            String token = Jwts.builder()
                    .setClaims(claims) // xem ở trong nó có cái j
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    // cau hoi bao mat
                    .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
       throw  new InvalidParamException("lỗi bà mày r kìa " + e.getMessage());
            //return null;
        }
    }

    private Key getSignInkey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);// Keys.hmacShaKeyFor(Decoders.BASE64.decode("Z3H2ehx2Aes52wCgLUyfJI/5BWJdcoRLnM17sdfSjbI="))
        return Keys.hmacShaKeyFor(bytes);
    }


    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJwt(token)
                .getBody();

    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);

    }


    // kiem tra xem token da het han chua
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());

    }
    // check xuat phon number ra token
    public String extractPhoneNumber(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // xem tken het han chua phone number co trung ko
    public boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }
}
