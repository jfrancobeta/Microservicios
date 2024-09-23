package com.formacionbdi.springboot.app.gateway.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;



@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(authHttp -> authHttp
                // Rutas permitidas sin autenticación
                .pathMatchers("/authorized").permitAll() // Endpoint para generar el token
                .pathMatchers("/api/security/**").permitAll()
                
                // Rutas protegidas con diferentes scopes
                .pathMatchers(HttpMethod.GET, "/api/productos/**").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                .pathMatchers(HttpMethod.POST, "/api/productos/**").hasAuthority("SCOPE_write")
                .pathMatchers(HttpMethod.GET, "/api/items/**").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                .pathMatchers(HttpMethod.POST, "/api/items/**").hasAuthority("SCOPE_write")
                .pathMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                .pathMatchers(HttpMethod.POST, "/api/usuarios/**").hasAuthority("SCOPE_write")
                
                // Cualquier otra solicitud necesita autenticación
                .anyExchange().authenticated())
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF ya que no se maneja con formulario
            .oauth2Login(Customizer.withDefaults()) // Página de inicio de sesión OAuth2
            .oauth2Client(Customizer.withDefaults()) // Configuración por defecto para el cliente OAuth2
            .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults())); // Configuración por defecto para el servidor de recursos con JWT
        
        return http.build();
    }
}
