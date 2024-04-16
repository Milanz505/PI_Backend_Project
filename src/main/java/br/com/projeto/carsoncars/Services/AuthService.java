package br.com.projeto.carsoncars.Services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.projeto.carsoncars.Entities.User.User;
import br.com.projeto.carsoncars.Repository.Repositorio;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SuppressWarnings("deprecation")
@Service
public class AuthService {

    @Autowired
    private Repositorio action;

    public ResponseEntity<?> authenticate(String email, String senha) {

        User user = action.findByEmail(email);

        if (user == null || !user.getSenha().equals(senha)) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        // Criação de uma chave segura para HS512
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        String token = Jwts.builder()
               .setSubject(user.getEmail())
               .setExpiration(new Date(System.currentTimeMillis() + 86400000))
               .signWith(key)
               .compact();

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
