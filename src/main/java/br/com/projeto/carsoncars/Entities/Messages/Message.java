package br.com.projeto.carsoncars.Entities.Messages;

import org.springframework.stereotype.Component;

@Component
public class Message {
    private String message;

    // Getters and setters

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
