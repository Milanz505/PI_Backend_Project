package br.com.projeto.carsoncars.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.projeto.carsoncars.Entities.Anuncio.Anuncio;

@Repository
public interface AnuncioRepository extends CrudRepository<Anuncio, UUID> {

    List<Anuncio> findByUserEmail(String email);

    Optional<Anuncio> findById(UUID id); // Change method name to findById

    void deleteById(UUID id);
}
