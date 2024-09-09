package com.registration.registration.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.registration.registration.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    // gestion des tokens
    private final String SECURITY_KEY = "75cf803b56d58eedf405bffa1ec75d8bcde528d078728c59bd6f7a2a814846c6";

    // method to extract the username from the claim
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public String generateToken(User user){
        return Jwts
                .builder()
                .subject(user.getUsername())
                .claim("firstname", user.getFirstname())
                .claim("lastname", user.getLastname())
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(getSiningKey())
                .compact();
                /* issuedAt : date d'émission
                 *  System.currentTimeMillis() : le nbr de milliseconde écoulé depuis le 1er janvier 1970
                 *  System.currentTimeMillis()): la date actuelle (le momment actuelle)
                 */
    }

    /* Cette méthode prend une clé secrète encodée en Base64,la décode en un tableau d'octets,
	* puis utilise cette représentation d'octets pour créer et retourner une clé secrète (SecretKey) utilisable
	* pour les opérations HMAC avec l'algorithme SHA. Cette clé secrète peut ensuite être utilisée pour signer
	* et vérifier l'intégrité des données à l'aide de HMAC.
	*/
    private SecretKey getSiningKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECURITY_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    // method to extract all the claims or payloads from the token
    public Claims extractAllClaims(String token){
        return Jwts
                .parser() // initialiser un Parseur. Ce parseur sera utiliser pour décoder et vérifier un jeton Jwt
                .verifyWith(getSiningKey()) // appel la méthode getSigningKey pour faire la vérification
                .build()
                .parseSignedClaims(token) // vérifie la signature de du token avec le signing key
                .getPayload(); // récupération du payload
    }

    // method to extract a specific payload
    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // method to check if the token is valid.
    public Boolean isTokenValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    // method to check the expiration tokens
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
}
