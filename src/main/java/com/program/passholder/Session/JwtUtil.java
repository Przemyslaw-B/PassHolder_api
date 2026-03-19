package com.program.passholder.Session;

import com.program.passholder.Database.Querry.User.Authentication.SetIsAuthorizatedStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Autowired
    SetIsAuthorizatedStatus setIsAuthorizatedStatus;

    //private final Key key= Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Key key;
    private final long expiration = 1000*60*60*24;  //24H

    public JwtUtil(@Value("${jwt.secret}")String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email){
        setIsAuthorizatedStatus.setAuthStatus(email, 0);    // Przy generowaniu nowego tokenu zresetuj status 2FA
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}
