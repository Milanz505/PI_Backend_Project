package br.com.projeto.carsoncars.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.projeto.carsoncars.Entities.User.User;
import br.com.projeto.carsoncars.Repository.Repositorio;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
    // Chave secreta JWT definida manualmente
    private static final String SECRET_KEY = "r1TCaDCyS+mT+eQgYHhiLVhO/VgApif3xdKpD7NWbjWfP0q9CGY7Xo78t/gfyUxjbfXhCnvNvwUHwBhREkFpGg==";

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeta o encoder de senha

    @Autowired
    private Repositorio action;

    public ResponseEntity<?> authenticate(String email, String senha) {
        User user = action.findByEmail(email);

        if (user == null || !passwordEncoder.matches(senha, user.getSenha())) {
            return new ResponseEntity<>("Email ou senha inválidos", HttpStatus.UNAUTHORIZED);
        }

        // Construir o token JWT usando a chave secreta definida manualmente
        @SuppressWarnings("deprecation")
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();

        // Atualizar a senha encriptada do usuário
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        user.setConfirmarSenha(passwordEncoder.encode(user.getConfirmarSenha()));

        // Criar um mapa para a resposta
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user); // Adicionar o usuário ao mapa

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
