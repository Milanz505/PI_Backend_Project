package br.com.projeto.carsoncars.Controle;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import br.com.projeto.carsoncars.ConfigChat.ObjMsg;

@Controller
public class App {

    @MessageMapping("/chatMessage")
    @SendTo("/canal")
    public ObjMsg sendMessage(ObjMsg message){
        return message;
    }

}