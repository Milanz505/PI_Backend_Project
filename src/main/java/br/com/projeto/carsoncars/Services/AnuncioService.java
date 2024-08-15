package br.com.projeto.carsoncars.Services;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
                                           String ano, float preco, String descricao, String email, String[] imageUrl) {

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


}