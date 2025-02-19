//package com.oauth.KeyCloakOAuth.security;

/*import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;*/

/**
 * Manual implementation of JWT authentication converter.
 *
 * This class is written because, after obtaining the token from Keycloak, the default JWT configuration
 * in Spring Boot transforms the access token into a Spring Boot-specific token. Internally, this
 * configuration uses the "scope" section as the source for roles. However, in our case, the actual roles
 * are located in the "realm_access" section of the JWT token.
 *
 * To address this, we have customized the JwtAuthConverter class to extract roles from the "realm_access"
 * section instead of the default "scope" section.
 */

/*@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> roles = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, roles);
    }

    public Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<GrantedAuthority> roles = new ArrayList<>();

        if (jwt.getClaim("realm_access") != null) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess.get("roles") != null) {
                List<String> keyCloakRoles = (List<String>) realmAccess.get("roles");
                for (String keyCloakRole : keyCloakRoles) {
                    roles.add(new SimpleGrantedAuthority("ROLE_" + keyCloakRole));
                    System.out.println(roles);
                }

            }
        }
        return roles;
    }
}*/