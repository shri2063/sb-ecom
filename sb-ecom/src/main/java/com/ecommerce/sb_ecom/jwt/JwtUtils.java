package com.ecommerce.sb_ecom.jwt;

import com.ecommerce.sb_ecom.security.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;



@Component
public class JwtUtils
{
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${jwtCookieName}")
    private String jwtCookie;

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

    public String getJwtFromCookies(HttpServletRequest request)
    {
        Cookie cookie = WebUtils.getCookie(request,jwtCookie);
        if(cookie != null)
        {
            return cookie.getValue();
        }
        else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal)
    {
      String jwt = generateTokenFromUsername(userPrincipal);
      ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).
              path("/api") .
              maxAge(24*60*60) .
              httpOnly(false).
              build();
      return cookie;
    }
    private   String generateTokenFromUsername(UserDetails userDetails)
    {
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                //.expiration(new Date(new Date().getTime() +  60*1000000000 ))
                .signWith(key())
                .compact();
    }

    public ResponseCookie getCLeanJwtCookie()
    {
        ResponseCookie responseCookie = ResponseCookie.from(jwtCookie,null).path("/api").build();
        return responseCookie;
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
