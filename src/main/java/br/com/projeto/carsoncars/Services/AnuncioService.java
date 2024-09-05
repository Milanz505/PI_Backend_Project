package br.com.projeto.carsoncars.Services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.projeto.carsoncars.Entities.Anuncio.Anuncio;
import br.com.projeto.carsoncars.Entities.User.User;
import br.com.projeto.carsoncars.Repository.AnuncioRepository;
import br.com.projeto.carsoncars.Repository.Repositorio;




@Service
public class AnuncioService {


    @Autowired
    private Repositorio repositorio;

    @Autowired
    private AnuncioRepository action;

    public ResponseEntity<?> createAnuncio(String marca, String modelo, String tempoDeUso,
                                           String ano, float preco, String descricao, String email, List<String> imageUrl) {

        User user = (User) action.findByUserEmail(email);

        if (user == null) {
            return new ResponseEntity<>("Invalid user", HttpStatus.UNAUTHORIZED);
        }

        if (marca == null || modelo == null || ano == null || preco < 0 || descricao == null || descricao.length() > 300) {
            return new ResponseEntity<>("Invalid fields", HttpStatus.BAD_REQUEST);
        }

        if (imageUrl == null) {
            return new ResponseEntity<>("Error saving image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        

    
        Anuncio anuncio = new Anuncio();
        anuncio.setMarca(marca);
        anuncio.setModelo(modelo);
        anuncio.setTempoDeUso(tempoDeUso);
        anuncio.setAno(ano);
        anuncio.setPreco(preco);
        anuncio.setDescricao(descricao);
        anuncio.setImageUrl(imageUrl);
        anuncio.setUser(user);

        // Salvando o anúncio com o valor FIPE
        action.save(anuncio);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Anuncio created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    public ResponseEntity<?> addLike(UUID anuncioId, UUID userId) {
        Optional<Anuncio> anuncioOpt = action.findById(anuncioId);
        Optional<User> userOpt = repositorio.findById(userId);
    
        if (!anuncioOpt.isPresent() || !userOpt.isPresent()) {
            return new ResponseEntity<>("Anuncio or User not found", HttpStatus.NOT_FOUND);
        }
    
        Anuncio anuncio = anuncioOpt.get();
        User user = userOpt.get();
    
        if (!anuncio.getLikedByUsers().contains(user)) {
            anuncio.getLikedByUsers().add(user);
            user.getLikedAnuncios().add(anuncio);
    
            action.save(anuncio);
            repositorio.save(user);
        }
    
        return new ResponseEntity<>(anuncio.getLikedByUsers(), HttpStatus.OK);
    }

    public ResponseEntity<?> removeLike(UUID anuncioId, UUID userId) {
        Optional<Anuncio> anuncioOpt = action.findById(anuncioId);
        Optional<User> userOpt = repositorio.findById(userId);
    
        if (!anuncioOpt.isPresent() || !userOpt.isPresent()) {
            return new ResponseEntity<>("Anuncio or User not found", HttpStatus.NOT_FOUND);
        }
    
        Anuncio anuncio = anuncioOpt.get();
        User user = userOpt.get();
    
        if (anuncio.getLikedByUsers().contains(user)) {
            anuncio.getLikedByUsers().remove(user);
            user.getLikedAnuncios().remove(anuncio);
    
            action.save(anuncio);
            repositorio.save(user);
        }
    
        return new ResponseEntity<>(anuncio.getLikedByUsers(), HttpStatus.OK);
    }

public ResponseEntity<?> addComentario(UUID anuncioId, UUID userId, String numero, String comentario) {
    Optional<Anuncio> anuncioOpt = action.findById(anuncioId);
    Optional<User> userOpt = repositorio.findById(userId);

    if (!anuncioOpt.isPresent()) {
        return new ResponseEntity<>("Anuncio not found", HttpStatus.NOT_FOUND);
    }

    if (!userOpt.isPresent()) {
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    Anuncio anuncio = anuncioOpt.get();
    User user = userOpt.get();

    if (!anuncio.getLikedByUsers().contains(user)) {
        return new ResponseEntity<>("User has not liked this anuncio", HttpStatus.FORBIDDEN);
    }

    if (comentario != null && !comentario.isEmpty() && numero != null && !numero.isEmpty()) {
        String comentarioCompleto = "Número: " + numero + " - Comentário: " + comentario;
        anuncio.getComentarios().put(userId, comentarioCompleto);
        action.save(anuncio);
    }

    return new ResponseEntity<>(anuncio.getComentarios(), HttpStatus.OK);
}

public ResponseEntity<?> getComentarios(UUID anuncioId) {
    Optional<Anuncio> anuncioOpt = action.findById(anuncioId);

    if (!anuncioOpt.isPresent()) {
        return new ResponseEntity<>("Anuncio not found", HttpStatus.NOT_FOUND);
    }

    Anuncio anuncio = anuncioOpt.get();
    Map<String, Object> response = new HashMap<>();
    response.put("comentarios", anuncio.getComentarios());
    response.put("usuarios", anuncio.getUser()); // Assuming getUsuarios() method exists

    return new ResponseEntity<>(response, HttpStatus.OK);
}

public ResponseEntity<?> getLikes(UUID anuncioId) {
    Optional<Anuncio> anuncioOpt = action.findById(anuncioId);

    if (!anuncioOpt.isPresent()) {
        return new ResponseEntity<>("Anuncio not found", HttpStatus.NOT_FOUND);
    }

    Anuncio anuncio = anuncioOpt.get();
    return new ResponseEntity<>(anuncio.getLikedByUsers(), HttpStatus.OK);
}


public Page<Anuncio> filtroAnuncios(Map<String, String> requestBody, Pageable pageable) {
    String marca = requestBody.get("marca");
    String modelo = requestBody.get("modelo");
    String ano = requestBody.get("ano");
    String precoMinStr = requestBody.get("precoMin");
    String precoMaxStr = requestBody.get("precoMax");
    String query = requestBody.get("query");
    String anoMin = requestBody.get("anoMin");
    String anoMax = requestBody.get("anoMax");

    
    marca = (marca != null) ? marca.trim() : "";
    modelo = (modelo != null) ? modelo.trim() : "";
    ano = (ano != null) ? ano.trim() : "";
    float precoMin = (precoMinStr != null) ? Float.parseFloat(precoMinStr) : 0;
    float precoMax = (precoMaxStr != null) ? Float.parseFloat(precoMaxStr) : Float.MAX_VALUE;
    query = (query != null) ? query.trim() : "";
    anoMin = (anoMin != null) ? anoMin.trim() : "";
    anoMax = (anoMax != null) ? anoMax.trim() : "";

    if(precoMin < 0 || precoMin > precoMax) {
        precoMin = 0;
    }

    if (precoMin < 0 || precoMin > precoMax) {
        precoMin = 0;
    }

    if (anoMin.isEmpty() || anoMax.isEmpty()) {
        anoMin = "0";
        anoMax = "9999";
    }
    if (marca.isEmpty() && modelo.isEmpty() && ano.isEmpty() && query.isEmpty() && precoMin == 0 && precoMax == Float.MAX_VALUE && anoMin.equals("0") && anoMax.equals("9999")) {
        return action.findAll(pageable);
    }
    
    return action.filtroAnuncios(marca, modelo, ano, precoMin, precoMax, query, anoMin, anoMax, pageable);

}

}