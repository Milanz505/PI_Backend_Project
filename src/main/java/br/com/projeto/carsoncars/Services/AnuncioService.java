package br.com.projeto.carsoncars.Services;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.projeto.carsoncars.Entities.Anuncio.Anuncio;
import br.com.projeto.carsoncars.Entities.User.User;
import br.com.projeto.carsoncars.Repository.AnuncioRepository;

@Service
public class AnuncioService {

    @Autowired
    private AnuncioRepository action;

    public ResponseEntity<?> createAnuncio(String modeloCarro, int ano, int preco, String descricao, String email,
            MultipartFile file) {

        User user = (User) action.findByUserEmail(email);

        if (user == null) {
            return new ResponseEntity<>("Invalid user", HttpStatus.UNAUTHORIZED);
        }

        if (modeloCarro == null || ano == 0 || preco < 0 || descricao == null || descricao.length() > 300) {
            return new ResponseEntity<>("Invalid fields", HttpStatus.BAD_REQUEST);
        }

        String imageUrl = saveImage(file);

        if (imageUrl == null) {
            return new ResponseEntity<>("Error saving image", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Anuncio anuncio = new Anuncio();
        anuncio.setImageUrl(imageUrl); // Adiciona a URL da imagem ao anúncio
        anuncio.setModeloCarro(modeloCarro);
        anuncio.setAno(Year.of(ano));
        anuncio.setPreco(preco);
        anuncio.setDescricao(descricao);
        anuncio.setUser(user);

        action.save(anuncio);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Anuncio created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<List<Anuncio>> readAnuncios() {
        List<Anuncio> anuncios = (List<Anuncio>) action.findAll();
        return new ResponseEntity<>(anuncios, HttpStatus.OK);
    }

    public ResponseEntity<?> updateAnuncio(UUID id, String modeloCarro, int ano, int preco, String descricao,
            String email) {
        Optional<Anuncio> optionalAnuncio = action.findById(id);

        if (optionalAnuncio.isEmpty()) {
            return new ResponseEntity<>("Anuncio not found", HttpStatus.NOT_FOUND);
        }

        Anuncio anuncio = optionalAnuncio.get();

        if (!anuncio.getUser().getEmail().equals(email)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (modeloCarro != null) {
            anuncio.setModeloCarro(modeloCarro);
        }

        if (ano != 0) {
            anuncio.setAno(Year.of(ano));
        }

        if (preco != -1) {
            anuncio.setPreco(preco);
        }

        if (descricao != null) {
            anuncio.setDescricao(descricao);
        }

        action.save(anuncio);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Anuncio updated successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteAnuncio(UUID id, String email) {
        Optional<Anuncio> optionalAnuncio = action.findById(id);

        if (optionalAnuncio.isEmpty()) {
            return new ResponseEntity<>("Anuncio not found", HttpStatus.NOT_FOUND);
        }

        Anuncio anuncio = optionalAnuncio.get(); // Extract the Anuncio object from Optional

        if (!anuncio.getUser().getEmail().equals(email)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        action.delete(anuncio); // Pass the Anuncio object to delete method

        Map<String, String> response = new HashMap<>();
        response.put("message", "Anuncio deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String saveImage(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            File destinationFile = new File("caminho/para/salvar/imagem/" + fileName);
            file.transferTo(destinationFile);
            return "caminho/para/salvar/imagem/" + fileName; // Retorna a URL da imagem
        } catch (IOException e) {
            e.printStackTrace();
            // Lógica para lidar com erros ao salvar a imagem
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            // Lógica para lidar com outros erros ao salvar a imagem
            return null;
        }
    }
}
