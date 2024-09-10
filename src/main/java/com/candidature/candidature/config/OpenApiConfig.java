package com.candidature.candidature.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Gats: Global Africa Technology System - Adresse: 17, cité horizon VDN",
                        email = "Email: gats@gatsmapping.com  -  Tel: +221 33 856 84 46  +221 660 34 34",
                        url = "https://gatsmapping.com"
                ),
                description = "Gats est une entreprise sénégalaise spécialisée dans les technologies",
                title = "Plateform pour l'etat major des forces armées",
                version = "1.0"

        ),
        servers = {
                @Server(
                        description = "server local",
                        url = "http://localhost:8089"
                ),

        }

)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
