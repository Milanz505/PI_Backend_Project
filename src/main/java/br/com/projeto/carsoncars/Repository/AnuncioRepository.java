package br.com.projeto.carsoncars.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.projeto.carsoncars.Entities.Anuncio.Anuncio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface AnuncioRepository extends CrudRepository<Anuncio, UUID> { // Tratamento de posts feito por ID

    List<Anuncio> findByUserEmail(String email);

    Optional<Anuncio> findById(UUID id); 

    void deleteById(UUID id);

    List<Anuncio> findByUserId(UUID id);

    Page<Anuncio> findAll(Pageable pageable);
}
