package com.registration.registration.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * @author cheikh diop
 *
 * Configuration OpenAPI pour la documentation de l'API REST.
 *
 * Cette classe configure les métadonnées pour l'API, y compris les informations
 * de contact, la description de l'application, et la configuration de la sécurité
 * utilisant des JWT pour l'authentification.
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Gats: Global Africa Technology System - Adresse: 17, cité horizon VDN",
                        email = "Email: gats@gatsmapping.com  -  Tel: +221 33 856 84 46  +221 660 34 34",
                        url = "https://gatsmapping.com"
                ),
                description = "Gats est une entreprise sénégalaise spécialisée dans les technologies.",
                title = "Plateforme pour l'état major des forces armées",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Serveur local pour le développement",
                        url = "http://localhost:8091"
                ),
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Description de l'authentification JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
