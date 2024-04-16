package br.com.projeto.carsoncars.Controle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.carsoncars.Entities.User.User;

@RestController
public class Controle {
    @GetMapping("")
    public String mensagem(){
        return "oiiiii :3";
    }
    @PostMapping("/User")
    public User user(@RequestBody User u){
        return u;
    }
}
