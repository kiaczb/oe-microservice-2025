package hu.oe.yokudlela.iam.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String ROLE_SEPARATOR = "::";
    private static final String SPRING_ROLE_PREFIX = "ROLE_";
    private static final String JWT_PATH = "resource_access";
    //    private static final String JWT_PATH = "realm_access";
    private static final String ROLES_LIST="roles";

    @Value("${server.name:}")
    private String resourceName;


    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap(JWT_PATH);
        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) ((Map<String, Object>)resourceAccess.getOrDefault(resourceName,Collections.emptyMap())).getOrDefault(ROLES_LIST, Collections.emptyList());
//        List<String> roles = (List<String>) resourceAccess.getOrDefault(ROLES_LIST, Collections.emptyList());
        return roles.stream()
                .map(CustomJwtAuthenticationConverter::convertRolesToSimpleGrantedAuthority)
                .toList();
    }

    private static GrantedAuthority convertRolesToSimpleGrantedAuthority(String role) {
        String rights = role;
        log.info("{}-{}", rights, rights.indexOf(ROLE_SEPARATOR));
        if (rights.contains(ROLE_SEPARATOR)) {
            String[] partOfRole = rights.split(ROLE_SEPARATOR);
            rights = partOfRole[partOfRole.length - 1];
        }
        return new SimpleGrantedAuthority( SPRING_ROLE_PREFIX+rights);
    }



}