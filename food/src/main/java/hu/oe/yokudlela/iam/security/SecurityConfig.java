package hu.oe.yokudlela.iam.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] allowedOrigins ={"*"};

    private static final String[] allowedMethods ={"*"};

    final CustomJwtAuthenticationConverter grantedAuthoritiesConverter;

    final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }


    @Bean
    SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http,
                                                          Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter) throws Exception {
        log.info("Initializing SecurityFilterChain");

        http.oauth2ResourceServer(resourceServer ->
                resourceServer.jwt(jwtDecoder ->
                        jwtDecoder.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        http.sessionManagement(sessions ->
                sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(requests -> {
            requests.requestMatchers(
                    "/v3/api-docs/**",
                    "/v3/api-docs",
                    "/swagger-ui/**",
                    "/swagger-ui.html").permitAll();
            requests.requestMatchers("/**").authenticated();
        });


        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of(allowedOrigins));
            configuration.setAllowedMethods(List.of(allowedMethods));
            configuration.setAllowCredentials(true);

            configuration.setAllowedHeaders(List.of("*"));
            configuration.setExposedHeaders(List.of("Authorization"));

            return configuration;
        }));

        return http.build();
    }


}