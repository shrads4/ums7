package com.nst.oauth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nst.oauth.config.TokenGenerator;
import com.nst.oauth.dto.ErrorDTO;
import com.nst.oauth.dto.LoginDTO;
import com.nst.oauth.dto.ResponseDTO;
import com.nst.oauth.dto.TokenDTO;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @Value("${wso2-token-url}")
    String wso2TokenURL;

    @Value("${wso2-client-token}")
    String wso2Token;

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

    @PostMapping("/getWso2Token")
    public ResponseEntity getToken() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, JsonProcessingException {

//  ------------------- TO MAKE REQ WITH CERT ---------
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((X509Certificate[] certificateChain, String authType) -> true)  // <--- accepts each certificate
                .build();

        Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(URIScheme.HTTPS.getId(), new SSLConnectionSocketFactory(sslContext))
                .register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory())
                .build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
                .setConnectionManagerShared(true)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + wso2Token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> bodyParamMap = new HashMap<>();
        bodyParamMap.put("grant_type","client_credentials");

        String reqBodyData = new ObjectMapper().writeValueAsString(bodyParamMap);
        HttpEntity<String> requestEnty = new HttpEntity<>(reqBodyData, headers);
        ResponseEntity<String> exchange = restTemplate.postForEntity(wso2TokenURL, requestEnty, String.class);
        JsonObject resultJsonObject = new JsonParser().parse(exchange.getBody()).getAsJsonObject();
        TokenDTO accessToken = TokenDTO.builder().accessToken(resultJsonObject.get("access_token").getAsString()).build();
        ResponseDTO<Object> result = ResponseDTO.builder()
                .status(Boolean.TRUE)
                .result(accessToken)
                .build();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
