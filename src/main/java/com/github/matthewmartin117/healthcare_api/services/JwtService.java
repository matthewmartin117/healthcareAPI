package com.github.matthewmartin117.healthcare_api.services;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

// JWT stores the username in the token (subject)
// token is signed with a secret key
// val

@Service
public class JwtService {
   // secret key for signing the JWTs, is hashed for security
    private static final String SECRET = "ZI6VdhaSemY8oUnVyXheyCSuaHK43bsq";
    // token validity period (e.g., 24 hours)
    private static final long EXPIRATION = 60 * 60 * 1000; // 1 hour in milliseconds
    // key used for signing and verifying JWTs
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // generate a JWT for a given username
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // gets a token , extracts username from it by getting claim instance
    // claim is is in the form of key value pairs
    // ex. ()"username": "admin")
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // validate the token by checking username
      public boolean validateToken(String token) {
        try {
          // parse the token to ensure it's valid
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // if parsing is successful, the token is valid
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}
