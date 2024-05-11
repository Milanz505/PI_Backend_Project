package br.com.projeto.carsoncars.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.projeto.carsoncars.Entities.Messages.Message;
import br.com.projeto.carsoncars.Entities.User.User;
import br.com.projeto.carsoncars.Repository.Repositorio;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SuppressWarnings("deprecation")
@Service
public class UserService {
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    
    @Autowired
    private Message message;

    @Autowired
    private Repositorio action;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeta o encoder de senha

    public ResponseEntity<?> cadastrar(User obj){

        if(obj.getNome().isEmpty()){
            message.setMessage("O nome precisa ser preenchido !!!");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if(!isValidEmail(obj.getEmail())) {
            message.setMessage("O endereço de email é inválido !!!");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if(!isValidPassword(obj.getSenha()) || !isConfirmPasswordValid(obj.getSenha(), obj.getConfirmarSenha())) {
            message.setMessage("A senha não atende aos requisitos mínimos ou as senhas não conferem !!!");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            // Encripta a senha antes de salvar
            obj.setSenha(passwordEncoder.encode(obj.getSenha()));
            obj.setConfirmarSenha(passwordEncoder.encode(obj.getConfirmarSenha()));
            User savedUser = action.save(obj);

            // Construa o token
            String jws = Jwts.builder()
                .setSubject(savedUser.getEmail())  // Defina o assunto do token (geralmente, o nome de usuário ou ID do usuário)
                .signWith(key)  // Assine o token com a chave privada
                .compact();  // Construa o token

            // Crie um mapa para a resposta
            Map<String, Object> response = new HashMap<>();
            response.put("user", savedUser);
            response.put("token", jws);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    
    
    }

    // Método para verificar se o e-mail é válido
    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Método para verificar se a senha atende aos requisitos mínimos
    private boolean isValidPassword(String password) {
        // uma senha deve ter pelo menos 8 caracteres
        // e conter pelo menos um número, uma letra minúscula, uma letra maiúscula e um caractere especial
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // Método para verificar se a senha de confirmação é igual à senha
    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
