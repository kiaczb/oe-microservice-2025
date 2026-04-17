package hu.oe.yokudlela.iam.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        type = SecuritySchemeType.OPENIDCONNECT,
        name = "OpenAPI",
        description = "KeyCloak Local dev from IDE",
        openIdConnectUrl = "http://localhost:9080/realms/oe/.well-known/openid-configuration")
@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8080/", description = "local dev"),
        },
        info = @Info(
                title = "OE Yokulela - Food  API",
                version = "v1",
                description = "Vevők",
                license = @License(name = "OE", url = "https://www.uni-obuda.hu/java-micro/license"),
                contact = @Contact(url = "...", name = "......", email = "kiaf@4ig.hu")))
@Configuration
public class OpenApiConfiguration {
}