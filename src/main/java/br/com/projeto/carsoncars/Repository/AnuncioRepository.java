package br.com.projeto.carsoncars.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.com.projeto.carsoncars.Entities.Anuncio.Anuncio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface AnuncioRepository extends CrudRepository<Anuncio, UUID> { // Tratamento de posts feito por ID

    List<Anuncio> findByUserEmail(String email);

    Optional<Anuncio> findById(UUID id); 

    void deleteById(UUID id);

    List<Anuncio> findByUserId(UUID id);

    Page<Anuncio> findAll(Pageable pageable);

    
    @Query("SELECT a FROM Anuncio a WHERE a.preco BETWEEN :precoMin AND :precoMax")
    List<Anuncio> findByPrecoBetween(@Param("precoMin") float precoMin, @Param("precoMax") float precoMax);

    List<Anuncio> findByMarca(String marca);

    List<Anuncio> findByModelo(String modelo);

    List<Anuncio> findByAno(String ano);

    @Query("SELECT a FROM Anuncio a WHERE a.ano BETWEEN :anoMin AND :anoMax")
    List<Anuncio> findByAnoBetween(@Param("anoMin") String anoMin, @Param("anoMax") String anoMax);


    @Query("SELECT a FROM Anuncio a WHERE a.ano LIKE %:query% OR a.modelo LIKE %:query% OR a.marca LIKE %:query%")
    List<Anuncio> searchAnuncios(@Param("query") String query);
}
