package com.formacionbdi.springboot.app.gateway.Config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;




@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authHttp -> authHttp
                // Rutas permitidas sin autenticación
                .requestMatchers("/authorized").permitAll() // Endpoint para generar el token
                .requestMatchers("/api/security/**").permitAll()
                
                // Rutas protegidas con diferentes scopes
                .requestMatchers(HttpMethod.GET, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/items/**").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                .requestMatchers(HttpMethod.POST, "/api/items/**").hasAuthority("SCOPE_write")
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasAuthority("SCOPE_write")
                
                // Cualquier otra solicitud necesita autenticación
                .anyRequest().authenticated())
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF ya que no se maneja con formulario
            .oauth2Login(login -> login.loginPage("/oauth2/authorization/client-app")) // Página de inicio de sesión OAuth2
            .oauth2Client(Customizer.withDefaults()) // Configuración por defecto para el cliente OAuth2
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults())); // Configuración por defecto para el servidor de recursos con JWT
            .oauth2ResourceServer(resourceServer -> resourceServer.jwt(
                jwt -> jwt.jwtAuthenticationConverter(new Converter<Jwt,AbstractAuthenticationToken>(){

                    @Override
                    public AbstractAuthenticationToken convert(Jwt source) {
                        Collection<String> roles = source.getClaimAsStringList("roles");
                        Collection<GrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
                        
                        return new JwtAuthenticationToken(source, authorities);
                    }
                    
                })))
                .build();
            }
            
}
