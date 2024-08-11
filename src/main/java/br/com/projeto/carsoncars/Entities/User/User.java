package br.com.projeto.carsoncars.Entities.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.projeto.carsoncars.Entities.Anuncio.Anuncio;

@Entity
@Table(name = "User")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private String confirmarSenha;
    private boolean admState;
    private String fotoDePerfil;

    
    @ManyToMany(mappedBy = "likedByUsers")
    @JsonBackReference
    private Set<Anuncio> likedAnuncios = new HashSet<>();


    // Getters and setters
    public boolean getAdmState() {
        return admState;
    }   

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public void setAdmState(boolean admState) {
        this.admState = admState;
    }

    public String getfotoDePerfil() {
        return fotoDePerfil;
    }

    public void setfotoDePerfil(String fotoDePefil) {
        this.fotoDePerfil = fotoDePefil;
    }

    public Set<Anuncio> getLikedAnuncios() {
        return likedAnuncios;
    }

    public void setLikedAnuncios(Set<Anuncio> likedAnuncios) {
        this.likedAnuncios = likedAnuncios;
    }
}