package br.com.projeto.carsoncars.Controle;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import br.com.projeto.carsoncars.Services.AnuncioService;
import br.com.projeto.carsoncars.Entities.Anuncio.Anuncio;
import br.com.projeto.carsoncars.Repository.AnuncioRepository;
import br.com.projeto.carsoncars.Services.FipeService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ControleAnuncio {

    @Autowired
    private FipeService fipeService;

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private AnuncioRepository anuncioRepository;

    @PostMapping("/anuncio")
    public Anuncio createAnuncio(@RequestBody Anuncio anuncio) {
        try {
            
            anuncio.setValorFipe(fipeService.getFipe(anuncio.getMarca(), anuncio.getModelo(), anuncio.getAno()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anuncioRepository.save(anuncio);
    }

    @GetMapping("/anuncio")
    public Page<Anuncio> getAllAnuncios(Pageable pageable){
        return anuncioRepository.findAll(pageable);
    }

    @GetMapping("/anuncio/{id}")
    public Anuncio getAnuncioById(@PathVariable UUID id) {
        return anuncioRepository.findById(id).orElse(null);
    }

    @GetMapping("/anuncio/user-email/{email}")
    public List<Anuncio> getAnunciosByUserEmail(@PathVariable String email) {
        return anuncioRepository.findByUserEmail(email);
    }

    @GetMapping("/anuncio/id-user/{id}")
    public List<Anuncio> getAnunciosByUserId(@PathVariable UUID id) {
        return anuncioRepository.findByUserId(id);
    }
    

    @DeleteMapping("/anuncio/delete/{id}")
    public void deleteAnuncio(@PathVariable UUID id) {
        anuncioRepository.deleteById(id);
    }

    @PutMapping("/anuncio")
    public Anuncio updateAnuncio(@RequestBody Anuncio anuncio) {
        return anuncioRepository.save(anuncio);
    }

    @PostMapping("/anuncio/{anuncioId}/like/{userId}")
    public ResponseEntity<?> likeAnuncio(@PathVariable UUID anuncioId, @PathVariable UUID userId) {
        return anuncioService.addLike(anuncioId, userId);
    }

    @PostMapping("/anuncio/{anuncioId}/unlike/{userId}")
    public ResponseEntity<?> unlikeAnuncio(@PathVariable UUID anuncioId, @PathVariable UUID userId) {
        return anuncioService.removeLike(anuncioId, userId);
    }

}
