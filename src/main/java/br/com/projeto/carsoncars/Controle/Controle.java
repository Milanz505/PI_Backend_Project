package br.com.projeto.carsoncars.Controle;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.carsoncars.Entities.User.User;
import br.com.projeto.carsoncars.Repository.Repositorio;
import br.com.projeto.carsoncars.Services.AuthService;
import br.com.projeto.carsoncars.Services.UserService;

@RestController
public class Controle {

    @Autowired
    private AuthService authService;

    @Autowired
    private Repositorio action;

    @Autowired
    private UserService UserService;

    @PostMapping("/User")
    public ResponseEntity<?> Cadastrar(@RequestBody User obj){
        return UserService.cadastrar(obj);
    }

    @GetMapping("/User")
    public List<User> select(){
        return action.findAll();
    }
    @GetMapping("/User/{id}")
    public User getById(@PathVariable UUID id){
        Optional<User> userOptional = action.findById(id);
        return userOptional.orElse(null); 
    }
    
    @GetMapping("/User/email/{email}")
    public User getByEmail(@PathVariable String email){
        return action.findByEmail(email);
    }

    @DeleteMapping("/User/delete/{id}")
    public void remove(@PathVariable UUID id){
        User obj = getById(id);
        action.delete(obj);

    }

    @PutMapping("/User")
    public User edit(@RequestBody User obj){
        return action.save(obj);
    }

    @GetMapping("/User/count")
    public long contador(){
        return action.count();
    }

    @GetMapping("/User/nomeContem")
    public List<User> nomeContem(){
        return action.findByNomeContaining("null");
    }

    @GetMapping("/User/iniciaCom")
    public List<User> nomeInicia(){
        return action.findByNomeStartsWith("null");
    }

    @GetMapping("/User/terminaCom")
    public List<User> nomeTermina(){
        return action.findByNomeEndsWith("null");
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");
        return authService.authenticate(email, senha);
    }



    @GetMapping("")
    public String mensagem(){
        return "oiiiii :3";
    }
    @PostMapping("/carsoncars")
    public User user(@RequestBody User u){
        return u;
    }
}
