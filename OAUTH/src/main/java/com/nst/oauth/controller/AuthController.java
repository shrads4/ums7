package com.nst.oauth.controller;

import com.nst.oauth.config.TokenGenerator;
import com.nst.oauth.dto.ErrorDTO;
import com.nst.oauth.dto.LoginDTO;
import com.nst.oauth.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    TokenGenerator tokenGenerator;
    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    @Qualifier("jwtAccessTokenAuthProvider")
    JwtAuthenticationProvider accessTokenAuthProvider;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword()));
        ResponseDTO result = ResponseDTO.builder()
                .status(authentication.isAuthenticated())
                .result(tokenGenerator.createToken(authentication))
                .build();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<ResponseDTO> validateToken(@RequestBody String token) {
        try {
            System.out.println(token);
            String brToken = token.split("=")[1].strip();
            Authentication authentication = accessTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(brToken));
            System.out.println("authentication.isAuthenticated() "+authentication.isAuthenticated());
            ResponseDTO<Object> result = ResponseDTO.builder()
                    .status(authentication.isAuthenticated())
                    .result("Authenticated successfully")
                    .build();
            return new ResponseEntity<>(result,HttpStatus.OK);


        } catch (Exception e){
            ResponseDTO<Object> result = ResponseDTO.builder()
                    .status(Boolean.FALSE)
                    .error(List.of(new ErrorDTO("JWT-DECODE","UN_AUTHORIZED")))
                    .build();
            return new ResponseEntity<>(result,HttpStatus.OK);

        }
    }
}
