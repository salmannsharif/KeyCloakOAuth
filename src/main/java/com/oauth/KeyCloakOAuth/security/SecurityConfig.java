package com.oauth.KeyCloakOAuth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //-> You need to enable this for method level authorization
public class SecurityConfig {

    /*private final JwtAuthConverter jwtAuthConverter;

    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/restaurants/public/**").permitAll()
                                .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(configure->
//                                        configure.jwtAuthenticationConverter(jwtAuthConverter))) -> Custom configuration
                        oauth2.jwt(Customizer.withDefaults()))
//                        oauth2.opaqueToken(Customizer.withDefaults())) -> Opaque token configuration
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }


    // This method removes the "ROLE_" prefix from role names, converting "ROLE_owner" to "owner"
    @Bean
    public DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(){
        DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        defaultMethodSecurityExpressionHandler.setDefaultRolePrefix("");
        return defaultMethodSecurityExpressionHandler;
    }


    // Using Opaque Token

//    @Bean
//    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
//        return new NimbusOpaqueTokenIntrospector(
//                "http://localhost:8081/realms/myrealm/protocol/openid-connect/token/introspect",
//                "spring-boot-client",
//                "na4hBdtOh4WmBHniC3ht4Knjr8NhqCS5"
//        );
//    }

}