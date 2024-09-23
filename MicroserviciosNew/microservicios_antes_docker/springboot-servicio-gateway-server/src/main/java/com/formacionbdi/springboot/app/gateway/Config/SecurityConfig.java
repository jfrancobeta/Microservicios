package com.formacionbdi.springboot.app.gateway.Config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;




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
                .pathMatchers(HttpMethod.GET, "/api/productos/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST, "/api/productos/**").hasRole("USER")
                .pathMatchers(HttpMethod.GET, "/api/items/**").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                .pathMatchers(HttpMethod.POST, "/api/items/**").hasAuthority("SCOPE_write")
                .pathMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                .pathMatchers(HttpMethod.POST, "/api/usuarios/**").hasAuthority("SCOPE_write")
                
                // Cualquier otra solicitud necesita autenticación
                .anyExchange().authenticated())
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF ya que no se maneja con formulario
            .oauth2Login(Customizer.withDefaults()) // Página de inicio de sesión OAuth2
            .oauth2Client(Customizer.withDefaults()) // Configuración por defecto para el cliente OAuth2
            //.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults())); // Configuración por defecto para el servidor de recursos con JWT
            .oauth2ResourceServer(resourceServer -> resourceServer.jwt(
                jwt -> jwt.jwtAuthenticationConverter(new Converter<Jwt,Mono<AbstractAuthenticationToken>>(){

                    @Override
                    public Mono<AbstractAuthenticationToken> convert(Jwt source) {
                        Collection<String> roles = source.getClaimAsStringList("roles");
                        Collection<GrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
                        
                        return Mono.just(new JwtAuthenticationToken(source, authorities));
                    }
                    
                })
            ));
        return http.build();
    }
}
