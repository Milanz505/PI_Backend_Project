package br.com.projeto.carsoncars.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.projeto.carsoncars.Entities.Messages.Message;
import br.com.projeto.carsoncars.Entities.User.User;
import br.com.projeto.carsoncars.Entities.User.UserUpdateDTO;
import br.com.projeto.carsoncars.Repository.Repositorio;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.sql.Date;

@SuppressWarnings("deprecation")
@Service
public class UserService {	
    // Chave secreta JWT definida manualmente (substitua esta chave pela sua própria)
    private static final String SECRET_KEY = "r1TCaDCyS+mT+eQgYHhiLVhO/VgApif3xdKpD7NWbjWfP0q9CGY7Xo78t/gfyUxjbfXhCnvNvwUHwBhREkFpGg==";


    @Autowired
    private Message message;

    @Autowired
    private Repositorio action;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeta o encoder de senha

    public ResponseEntity<?> cadastrar(User obj){
        
        User existingUser = action.findByNome(obj.getNome());

        if(obj.getNome().isEmpty()){
            message.setMessage("O nome precisa ser preenchido !!!");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if(!isValidEmail(obj.getEmail())) {
            message.setMessage("O endereço de email é inválido !!!");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if(!isValidPassword(obj.getSenha()) || !isConfirmPasswordValid(obj.getSenha(), obj.getConfirmarSenha())) {
            message.setMessage("A senha não atende aos requisitos mínimos ou as senhas não conferem !!!");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);  
        }else if (action.findByEmail(obj.getEmail()) != null) {
            message.setMessage("O endereço de email já está em uso !!!");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);   
        }
        else if (existingUser != null && !existingUser.getId().equals(obj.getId())) {
            message.setMessage("O nome já está em uso !!!");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);   
        }
        
         else {
            // Encripta a senha antes de salvar
            obj.setSenha(passwordEncoder.encode(obj.getSenha()));
            obj.setConfirmarSenha(passwordEncoder.encode(obj.getConfirmarSenha()));
            
            User savedUser = action.save(obj);

            long expirationTime = 60 * 60 * 1000; // 1 hora em milissegundos

            String jws = Jwts.builder()
               .setSubject(savedUser.getId().toString())
               .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Defina o tempo de expiração
               .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
               .compact(); 

            // Crie um mapa para a resposta
            Map<String, Object> response = new HashMap<>();
            response.put("user", savedUser);
            response.put("token", jws);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

    } 
    public User updateUser(UUID id, UserUpdateDTO userUpdateDTO) {
        User user = action.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
    
        // Atualizar nome se estiver presente e não vazio
        if (userUpdateDTO.getNome() != null) {
            if (userUpdateDTO.getNome().isEmpty()) {
                userUpdateDTO.setNome(user.getNome());
            } else {
                if (!isValidNome(userUpdateDTO.getNome())) {
                    throw new IllegalArgumentException("O nome é inválido.");
                } else {
                    // Verifica se já existe um usuário com o mesmo nome
                    User existingUser = action.findByNome(userUpdateDTO.getNome());
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        throw new IllegalArgumentException("O nome já está em uso.");
                    }
                    user.setNome(userUpdateDTO.getNome());
                }
            }
        }
    
        // Atualizar email se estiver presente e não vazio
        if (userUpdateDTO.getEmail() != null) {
            if (userUpdateDTO.getEmail().isEmpty()) {
                userUpdateDTO.setEmail(user.getEmail());
            } else {
                if (!isValidEmail(userUpdateDTO.getEmail())) {
                    throw new IllegalArgumentException("O endereço de email é inválido.");
                }
                else if (action.findByEmail(userUpdateDTO.getEmail()) != null) {
                    throw new IllegalArgumentException("O endereço de email já está em uso.");
                }
                user.setEmail(userUpdateDTO.getEmail());
            }
        }
    
        // Validação e atualização de senha
        if (userUpdateDTO.getSenha() != null) {
            if (userUpdateDTO.getSenha().isEmpty()) {
                userUpdateDTO.setSenha(user.getSenha());
            } else {
                if (!userUpdateDTO.getSenha().equals(userUpdateDTO.getConfirmarSenha())) {
                    throw new IllegalArgumentException("As senhas não conferem.");
                }
                if (!isValidPassword(userUpdateDTO.getSenha())) {
                    throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres, incluindo um número, uma letra minúscula, uma letra maiúscula e um caractere especial.");
                }
                user.setSenha(passwordEncoder.encode(userUpdateDTO.getSenha()));
            }
        }
    
        // Atualizar foto de perfil se estiver presente
        if (userUpdateDTO.getFotoDePerfil() != null) {
            if (userUpdateDTO.getFotoDePerfil().isEmpty()) {
                userUpdateDTO.setFotoDePerfil(user.getFotoDePerfil());
            } else {
                user.setFotoDePerfil(userUpdateDTO.getFotoDePerfil());
            }
        }
        
        user.setAdmState(userUpdateDTO.getAdmState());
    
        return action.save(user);
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

    private boolean isValidNome(String nome) {
        String regex = "^(?=.*[A-Z])[a-zA-Z0-9]{5,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nome);
        return matcher.matches();
    }
    
      

    // Método para verificar se a senha de confirmação é igual à senha
    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
