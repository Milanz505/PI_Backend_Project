package br.com.projeto.carsoncars.Controle;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.carsoncars.Services.FipeService;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/fipe")
public class FipeControle {

    @Autowired
    private FipeService fipeService;

    @GetMapping("/marcas")
    public ResponseEntity<String> getMarcas() {
        try {
            String marcas = fipeService.getMarcas();
            return ResponseEntity.ok(marcas);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao buscar marcas: " + e.getMessage());
        }
    }

    @GetMapping("/marcas/{marcaId}/modelos")
    public ResponseEntity<String> getModelos(@PathVariable String marcaId) {
        try {
            String modelos = fipeService.getModelos(marcaId);
            return ResponseEntity.ok(modelos);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao buscar modelos: " + e.getMessage());
        }
    }

    @GetMapping("/marcas/{marcaId}/modelos/{modeloId}/anos")
    public ResponseEntity<String> getAnos(@PathVariable String marcaId, @PathVariable String modeloId) {
        try {
            String anos = fipeService.getAnos(marcaId, modeloId);
            return ResponseEntity.ok(anos);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao buscar anos: " + e.getMessage());
        }
    }

    @GetMapping("/marcas/{marcaId}/modelos/{modeloId}/anos/{ano}")
    public ResponseEntity<String> getFipe(@PathVariable String marcaId, @PathVariable String modeloId, @PathVariable String ano) {
        try {
            String fipe = fipeService.getFipe(marcaId, modeloId, ano);
            return ResponseEntity.ok(fipe);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao buscar dados FIPE: " + e.getMessage());
        }
    }

    @GetMapping("/marcas/nome/{nomeMarca}")
    public ResponseEntity<String> getMarcaIdByName(@PathVariable String nomeMarca) {
        try {
            String marcaId = fipeService.getMarcaIdByName(nomeMarca);
            return ResponseEntity.ok(marcaId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao buscar ID da marca: " + e.getMessage());
        }
    }

    @GetMapping("/marcas/{marcaId}/modelos/nome/{nomeModelo}")
    public ResponseEntity<String> getModeloIdByName(@PathVariable String marcaId, @PathVariable String nomeModelo) {
        try {
            String modeloId = fipeService.getModeloIdByName(marcaId, nomeModelo);
            return ResponseEntity.ok(modeloId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao buscar ID do modelo: " + e.getMessage());
        }
    }
}
    


