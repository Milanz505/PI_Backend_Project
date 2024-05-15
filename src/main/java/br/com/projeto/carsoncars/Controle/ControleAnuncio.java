package br.com.projeto.carsoncars.Controle;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.projeto.carsoncars.Entities.Anuncio.Anuncio;
import br.com.projeto.carsoncars.Repository.AnuncioRepository;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ControleAnuncio {

    @Autowired
    private AnuncioRepository anuncioRepository;

    @PostMapping("/anuncio")
    public Anuncio createAnuncio(@RequestBody Anuncio anuncio) {
        return anuncioRepository.save(anuncio);
    }

    @GetMapping("/anuncio")
    public List<Anuncio> getAllAnuncios() {
        return (List<Anuncio>) anuncioRepository.findAll();
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
}
