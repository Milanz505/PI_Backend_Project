package br.com.projeto.carsoncars.Services;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
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

    public ResponseEntity<?> createAnuncio(String marca, String modelo, String nomeDoAutomovel, String tempoDeUso,
                                           Year ano, float preco, String descricao, String email, MultipartFile file) {

        User user = (User) action.findByUserEmail(email);

        if (user == null) {
            return new ResponseEntity<>("Invalid user", HttpStatus.UNAUTHORIZED);
        }

        if (marca == null || modelo == null || nomeDoAutomovel == null || ano == null || preco < 0 || descricao == null || descricao.length() > 300) {
            return new ResponseEntity<>("Invalid fields", HttpStatus.BAD_REQUEST);
        }

        String imageUrl = saveImage(file);

        if (imageUrl == null) {
            return new ResponseEntity<>("Error saving image", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Anuncio anuncio = new Anuncio();
        anuncio.setMarca(marca);
        anuncio.setModelo(modelo);
        anuncio.setNomeDoAutomovel(nomeDoAutomovel);
        anuncio.setTempoDeUso(tempoDeUso);
        anuncio.setAno(ano);
        anuncio.setPreco(preco);
        anuncio.setDescricao(descricao);
        anuncio.setImageUrl(imageUrl);
        anuncio.setUser(user);

        action.save(anuncio);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Anuncio created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private String saveImage(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            File destinationFile = new File("caminho/para/salvar/imagem/" + fileName);
            file.transferTo(destinationFile);
            return "caminho/para/salvar/imagem/" + fileName; // Retorna a URL da imagem
        } catch (IOException e) {
            e.printStackTrace();
            // add depois Lógica para lidar com erros ao salvar a imagem
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            // add depois Lógica para lidar com outros erros ao salvar a imagem
            return null;
        }
    }
}