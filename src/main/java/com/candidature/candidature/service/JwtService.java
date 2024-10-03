package com.candidature.candidature.service;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.candidature.candidature.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * @author cheikh diop
 *
 * Service de gestion des tokens JWT. Cette classe permet de créer, extraire et valider des tokens
 * basés sur JSON Web Tokens (JWT) pour l'authentification et l'autorisation des utilisateurs.
 */
@Service
public class JwtService {

    // Clé de sécurité utilisée pour signer les tokens
    private final String SECURITY_KEY = "75cf803b56d58eedf405bffa1ec75d8bcde528d078728c59bd6f7a2a814846c6";

    /**
     * Extrait le nom d'utilisateur du token.
     *
     * @param token le token JWT
     * @return le nom d'utilisateur contenu dans le token
     */
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait l'email du token.
     *
     * @param token le token JWT
     * @return l'email contenu dans le token
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    /**
     * Génère un token JWT pour un utilisateur donné.
     *
     * @param user l'utilisateur pour lequel le token est généré
     * @return le token JWT généré
     */
    public String generateToken(User user){
        return Jwts
                .builder()
                .subject(user.getUsername())
                .claim("firstname", user.getFirstname())
                .claim("lastname", user.getLastname())
                .claim("haveSubmited", user.getHave_postuled())
                .claim("role", user.getRole())
                .claim("typeCandidat", user.getType_candidat())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Obtient la clé secrète utilisée pour signer les tokens.
     *
     * @return la clé secrète pour HMAC
     */
    private SecretKey getSigningKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECURITY_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    /**
     * Extrait le rôle de l'utilisateur à partir du token.
     *
     * @param token le token JWT
     * @return le rôle de l'utilisateur
     */
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    /**
     * Extrait toutes les revendications (claims) du token.
     *
     * @param token le token JWT
     * @return les revendications contenues dans le token
     */
    public Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrait une revendication spécifique du token.
     *
     * @param token le token JWT
     * @param resolver fonction pour extraire la revendication
     * @param <T> type de la revendication
     * @return la valeur de la revendication
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Vérifie si le token est valide.
     *
     * @param token le token JWT
     * @param user les détails de l'utilisateur
     * @return true si le token est valide, false sinon
     */
    public Boolean isTokenValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Vérifie si le token est expiré.
     *
     * @param token le token JWT
     * @return true si le token est expiré, false sinon
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait la date d'expiration du token.
     *
     * @param token le token JWT
     * @return la date d'expiration
     */
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
}
