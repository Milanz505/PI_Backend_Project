package br.com.projeto.carsoncars.Entities.Anuncio;

import java.time.Year;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import br.com.projeto.carsoncars.Entities.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "anuncio") 
@EntityListeners(AuditingEntityListener.class)
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    @Column(name = "id_anuncio")
    private UUID id;

    @Column(name = "modelo_carro", nullable = false)
    private String modeloCarro;

    @Column(name = "ano", nullable = false)
    private Year ano;

    @Column(name = "preco", nullable = false)
    private int preco;

    @Column(name = "descricao", nullable = false, length = 300)
    private String descricao;

    @Column(name = "imagem_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Relação User->Anuncio
    private User user;

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getModeloCarro() {
        return modeloCarro;
    }

    public void setModeloCarro(String modeloCarro) {
        this.modeloCarro = modeloCarro;
    }

    public Year getAno() {
        return ano;
    }

    public void setAno(Year ano) {
        this.ano = ano;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


