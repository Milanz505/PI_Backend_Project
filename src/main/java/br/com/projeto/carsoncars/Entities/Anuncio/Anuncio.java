package br.com.projeto.carsoncars.Entities.Anuncio;


import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import br.com.projeto.carsoncars.Entities.User.User; 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne; 
import jakarta.persistence.Table;




@Entity
@Table(name = "anuncio")
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_anuncio")
    private UUID id;

    @Column(name = "marca", nullable = false)
    private String marca;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "nome_do_automovel", nullable = false)
    private String nomeDoAutomovel;

    @Column(name = "tempo_de_uso", nullable = false)
    private String tempoDeUso;

    @Column(name = "ano", nullable = false)
    private Year ano;

    @Column(name = "preco", nullable = false)
    private float preco;

    @Column(name = "descricao", nullable = false, length = 300)
    private String descricao;

    @Column(name = "imagem_url")
    private String[] imageUrl;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Relação User->Anuncio
    private User user;

    @ManyToMany
    @JoinTable(
        name = "anuncio_likes",
        joinColumns = @JoinColumn(name = "anuncio_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
        
    )
    private Set<User> likedByUsers = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Relação User->Anuncio


    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNomeDoAutomovel() {
        return nomeDoAutomovel;
    }

    public void setNomeDoAutomovel(String nomeDoAutomovel) {
        this.nomeDoAutomovel = nomeDoAutomovel;
    }

    public String getTempoDeUso() {
        return tempoDeUso;
    }

    public void setTempoDeUso(String tempoDeUso2) {
        this.tempoDeUso = tempoDeUso2;
    }

    public Year getAno() {
        return ano;
    }

    public void setAno(Year ano) {
        this.ano = ano;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(Set<User> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }

}