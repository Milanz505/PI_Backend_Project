package br.com.projeto.carsoncars.Controle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.carsoncars.Entities.Anuncio.Anuncio;
import br.com.projeto.carsoncars.Entities.User.User;
import br.com.projeto.carsoncars.Repository.AnuncioRepository;
import br.com.projeto.carsoncars.Repository.Repositorio;
import br.com.projeto.carsoncars.Services.AuthService;
import br.com.projeto.carsoncars.Services.UserService;
import br.com.projeto.carsoncars.Entities.User.UserUpdateDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Controle {

    @Autowired
    private AuthService authService;

    @Autowired
    private Repositorio action;

    @Autowired
    private UserService UserService;

    @Autowired
    private AnuncioRepository anuncioRepository;


    //USER 

    @PostMapping("/User")
    public ResponseEntity<?> Cadastrar(@RequestBody User obj){
        return UserService.cadastrar(obj);
    }

    @GetMapping("/User")
    public Page<User> getUsers(Pageable pageable){
        return action.findAll(pageable);
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
    User user = getById(id);
    List<Anuncio> anuncios = anuncioRepository.findByUserId(id);
    for (Anuncio anuncio : anuncios) {
        anuncioRepository.delete(anuncio);
    }
    action.delete(user);
}


    @PutMapping("/User/{id}")
    public User update(@PathVariable UUID id, @RequestBody UserUpdateDTO userUpdateDTO){
        return UserService.updateUser(id, userUpdateDTO);
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

    // AUTH

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String senha = loginRequest.getSenha();
        return authService.authenticate(email, senha);
    }
    

    // ANUNCIO



@GetMapping("/anuncio/count")
public long getAnuncioCount() {
    return action.count();
}


    // LOCALHOST


    @PostMapping("/carsoncars")
    public User user(@RequestBody User u){
        return u;
    }
}
