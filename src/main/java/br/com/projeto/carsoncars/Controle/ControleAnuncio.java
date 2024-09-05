package br.com.projeto.carsoncars.Controle;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    Pageable pageable = PageRequest.of(1, 20);


    @PostMapping("/anuncio")
    public ResponseEntity<?> createAnuncio(@RequestBody Anuncio anuncio) {
        try {
            // Obter o ID da marca pelo nome
            String marcaId = fipeService.getMarcaIdByName(anuncio.getMarca());
            if (marcaId == null) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Marca não encontrada: " + anuncio.getMarca());
            }
    
            // Obter o ID do modelo pelo nome e ID da marca
            String modeloId = fipeService.getModeloIdByName(marcaId, anuncio.getModelo());
            if (modeloId == null) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Modelo não encontrado: " + anuncio.getModelo());
            }
    
            // Obter o valor FIPE usando os IDs da marca e do modelo
            String valorFipe = fipeService.getFipe(marcaId, modeloId, anuncio.getAno());
            anuncio.setValorFipe(valorFipe);
    
            // Salvar o anúncio no banco de dados
            Anuncio savedAnuncio = anuncioRepository.save(anuncio);
            return ResponseEntity.status(HttpStatus.SC_CREATED).body(savedAnuncio);
    
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Erro ao buscar valor FIPE: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Ocorreu um erro: " + e.getMessage());
        }
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

    @PostMapping("/anuncio/{anuncioId}/comentario/{userId}")
    public ResponseEntity<?> addComentario(@PathVariable UUID anuncioId, @PathVariable UUID userId, @RequestBody Map<String, String> requestBody) {
        String numero = requestBody.get("numero");
        String comentario = requestBody.get("comentario");
        return anuncioService.addComentario(anuncioId, userId, numero, comentario);
    }
    
    @GetMapping("/anuncio/comentario/{anuncioId}")
    public ResponseEntity<?> getComentarios(@PathVariable UUID anuncioId) {
        return anuncioService.getComentarios(anuncioId);
    }    

    @GetMapping("/anuncio/{anuncioId}/likes")
    public ResponseEntity<?> getLikes(@PathVariable UUID anuncioId) {
        return anuncioService.getLikes(anuncioId);
    }

    @GetMapping("/anuncio/preco-entre/{precoMin}/{precoMax}")
    public Page<Anuncio> getAnunciosByPreco(@PathVariable float precoMin, @PathVariable float precoMax,
                                            Pageable pageable) {
        return anuncioRepository.findByPrecoBetween(precoMin, precoMax, pageable);
    }
    
    @GetMapping("/anuncio/marca/{marca}")
    public Page<Anuncio> getAnunciosByMarca(@PathVariable String marca, Pageable pageable) {
        return anuncioRepository.findByMarca(marca, pageable);
    }
    
    @GetMapping("/anuncio/modelo/{modelo}")
    public Page<Anuncio> getAnunciosByModelo(@PathVariable String modelo, Pageable pageable) {
        return anuncioRepository.findByModelo(modelo, pageable);
    }
    
    @GetMapping("/anuncio/ano/{ano}")
    public Page<Anuncio> getAnunciosByAno(@PathVariable String ano, Pageable pageable) {
        return anuncioRepository.findByAno(ano, pageable);
    }
    
    @GetMapping("/anuncio/ano-entre/{anoMin}/{anoMax}")
    public Page<Anuncio> getAnunciosByAnoBetween(@PathVariable String anoMin, @PathVariable String anoMax,
                                                 Pageable pageable) {
        return anuncioRepository.findByAnoBetween(anoMin, anoMax, pageable);
    }

    @GetMapping("/anuncio/search")
    public Page<Anuncio> searchAnuncios(@RequestParam String query, Pageable pageable) {
        return anuncioRepository.searchAnuncios(query, pageable);
    }

    @GetMapping("/anuncio/filtro")
    public Page<Anuncio> filtroAnuncios(@RequestBody Map<String, String> requestBody, Pageable pageable) {

        if(requestBody.isEmpty()){
            return anuncioRepository.findAll(pageable);
        }
        if(requestBody.get("precoMin").isEmpty()){
            requestBody.put("precoMin", "0");
        }
        if(requestBody.get("precoMax").isEmpty()){
            requestBody.put("precoMax", "999999999");
        }
        if(requestBody.get("ano").isEmpty()){
            requestBody.put("ano", "0");
        }
        if(requestBody.get("marca").isEmpty()){
            requestBody.put("marca", "");
        }
        if(requestBody.get("modelo").isEmpty()){
            requestBody.put("modelo", "");
        }
        if(requestBody.get("query").isEmpty()){
            requestBody.put("query", "");
        }
        

        String marca = requestBody.get("marca");
        String modelo = requestBody.get("modelo");
        String ano = requestBody.get("ano");
        String precoMin = requestBody.get("precoMin");
        String precoMax = requestBody.get("precoMax");
        String query = requestBody.get("query");
        return anuncioRepository.filtroAnuncios(marca, modelo, ano, precoMin, precoMax, query, pageable);
    }

}


