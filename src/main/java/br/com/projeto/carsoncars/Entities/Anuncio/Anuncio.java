package br.com.projeto.carsoncars.Entities.Anuncio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import br.com.projeto.carsoncars.Entities.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
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

    @Column(name = "tempo_de_uso", nullable = false)
    private String tempoDeUso;

    @Column(name = "ano", nullable = false)
    private String ano;

    @Column(name = "preco", nullable = false)
    private float preco;

    @Column(name = "descricao", nullable = false, length = 300)
    private String descricao;

    @ElementCollection
    @Column(name = "imagem_url")
    private List<String> imageUrl;

    @Column(name = "ValorFipe")
    private String valorFipe;

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

    @ElementCollection
    @MapKeyColumn(name = "user_id")
    @Column(name = "comentario")
    private Map<UUID, String> comentarios = new HashMap<>();

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

    public String getTempoDeUso() {
        return tempoDeUso;
    }

    public void setTempoDeUso(String tempoDeUso) {
        this.tempoDeUso = tempoDeUso;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
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

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
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

    public String getValorFipe() {
        return valorFipe;
    }

    public void setValorFipe(String valorFipe) {
        this.valorFipe = valorFipe;
    }

    public Map<UUID, String> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Map<UUID, String> comentarios) {
        this.comentarios = comentarios;
    }
}
