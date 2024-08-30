package br.com.projeto.carsoncars.Entities.User;

public class UserUpdateDTO {
    private String nome;
    private String email;
    private String senha;
    private String confirmarSenha;
    private String fotoDePerfil;
    private boolean admState;

    // Getters and setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String getFotoDePerfil() {
        return fotoDePerfil;
    }

    public void setFotoDePerfil(String fotoDePerfil) {
        this.fotoDePerfil = fotoDePerfil;
    }

    public void setAdmState(boolean admState){
        this.admState = admState;
    }

    public boolean getAdmState(){
        return this.admState;
    }

}
