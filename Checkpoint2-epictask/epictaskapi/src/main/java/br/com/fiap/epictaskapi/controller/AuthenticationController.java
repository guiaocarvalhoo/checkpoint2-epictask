package br.com.fiap.epictaskapi.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.fiap.epictaskapi.model.User;
import br.com.fiap.epictaskapi.model.Token;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Value("${token.secret}")
    String key;

    @Value("${token.expiration.time}")
    String EXPIRATION_TIME;
    
    @PostMapping
    public ResponseEntity<Object> auth(@RequestBody User user){

        try{
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
            User login = (User) authenticate.getPrincipal();
            Date createAt = new Date();
            Date expiresAt = new Date( createAt.getTime() + Long.valueOf(EXPIRATION_TIME) );
            String token = JWT.create()
                .withSubject(login.getId().toString())
                .withIssuedAt(createAt)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512(key)
            );
    
            var jwt = new Token(token, "Bearer");
            return ResponseEntity.ok(jwt);

        }catch(org.springframework.security.core.AuthenticationException e){
            throw new RuntimeException("Erro de authenticação");
        }

     

    }

}
