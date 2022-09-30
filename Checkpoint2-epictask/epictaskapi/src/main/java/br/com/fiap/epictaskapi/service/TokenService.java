package br.com.fiap.epictaskapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.fiap.epictaskapi.model.User;
import br.com.fiap.epictaskapi.repository.UserRepository;

public class TokenService {

    @Value("${token.secret}")
    String key;

    @Autowired
    UserRepository repository;
    
    public boolean validate(String token){
        try {
            JWT.require(Algorithm.HMAC512(key)).build().verify(token).getSubject();
            return true;
        }catch (Exception e){
            return false;
        }
            
    }

    public User getUser(String token) {
        String id = JWT.require(Algorithm.HMAC512(key)).build().verify(token).getSubject();
        Optional<User> optional = repository.findById(Long.valueOf(id));
        return optional.get();
    }

}
