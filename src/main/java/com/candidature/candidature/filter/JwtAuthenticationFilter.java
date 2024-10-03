package com.candidature.candidature.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.candidature.candidature.service.JwtService;
import com.candidature.candidature.service.UserDetailsServiceImp;

import java.io.IOException;

/**
 * @author cheikh diop
 *
 * Filtre d'authentification JWT qui intercepte les requêtes HTTP pour vérifier la présence d'un
 * token JWT dans l'en-tête Authorization. Si un token valide est trouvé, l'utilisateur est authentifié
 * et le contexte de sécurité est mis à jour.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImp userDetailsServiceImp;

    /**
     * Constructeur pour initialiser le filtre avec les services nécessaires.
     *
     * @param jwtService Service de gestion des tokens JWT.
     * @param userDetailsServiceImp Service pour charger les détails de l'utilisateur.
     */
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImp userDetailsServiceImp) {
        this.jwtService = jwtService;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    /**
     * Filtre interne qui intercepte chaque requête HTTP. Vérifie la présence et la validité du token JWT.
     *
     * @param request La requête HTTP entrante.
     * @param response La réponse HTTP à envoyer.
     * @param filterChain La chaîne de filtres à laquelle passer la requête.
     * @throws ServletException Si une erreur de servlet se produit.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
