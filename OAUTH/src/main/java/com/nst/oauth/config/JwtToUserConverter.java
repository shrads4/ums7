package com.nst.oauth.config;

import com.nst.oauth.repository.UserRepository;
import com.nst.oauth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Autowired
    UserRepository repository;
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        Optional<User> userFromDb = repository.findById(jwt.getSubject());
        List<String> roles = (List<String>) jwt.getClaims().get("roles");
        List<GrantedAuthority> collect = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userFromDb, jwt, collect);
    }
}
