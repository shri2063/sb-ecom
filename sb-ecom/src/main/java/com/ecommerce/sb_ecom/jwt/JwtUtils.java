package com.ecommerce.sb_ecom.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

import static jdk.internal.org.jline.keymap.KeyMap.key;

@Component
public class JwtUtils
{
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;
    @Value("$(spring.app.jwtExpirations)")
    private String jwtExpirations;

    public String getJwtFromHeader(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Bearer token: {}", bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7);
        }
        return null;

    }

    public  String generateTokenFromUsername(UserDetails userDetails)
    {
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirations))
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwtToken(String token)
    {
        return Jwts.parser()
                .verifyWith((SecretKey)key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public static final io. jsonwebtoken. io. Decoder<CharSequence, byte[]> BASE63 = null;
    private Key key()
    {
     return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    public boolean validateJwtToken(String authToken)
    {
        try{
            Jwts.parser()
                    .verifyWith((SecretKey)key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;

        }
        catch (ExpiredJwtException e)
        {
            logger.error("Expired JWT token");
        }
        return false;
    }

}
