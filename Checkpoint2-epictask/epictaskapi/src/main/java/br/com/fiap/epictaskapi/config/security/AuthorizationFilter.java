package br.com.fiap.epictaskapi.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.epictaskapi.model.User;
import br.com.fiap.epictaskapi.service.TokenService;

public class AuthorizationFilter extends OncePerRequestFilter {

    @Value("${token.secret}")
    String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
         
        // pegar o cabeçalho (Bearer dkjsfkjadsbfabsdkfjbasdkjbfakjdsarh)
        String header = request.getHeader("Authorization");

        // verificar se tem o prefixo bearer
        if (header == null || !header.startsWith("Bearer ") ) return;
        var token = header.substring(7);

        if (new TokenService().validate(token)){
            User user = new TokenService().getUser(token);
            Authentication authentication = 
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }      

        filterChain.doFilter(request, response);
        
    }

    
    
}
