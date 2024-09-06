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
    Page<Anuncio> findByPrecoBetween(@Param("precoMin") float precoMin, @Param("precoMax") float precoMax, Pageable pageable);

    Page<Anuncio> findByMarca(String marca, Pageable pageable);

    Page<Anuncio> findByModelo(String modelo, Pageable pageable);

    Page<Anuncio> findByAno(String ano, Pageable pageable);

    @Query("SELECT a FROM Anuncio a WHERE a.ano BETWEEN :anoMin AND :anoMax")
    Page<Anuncio> findByAnoBetween(@Param("anoMin") String anoMin, @Param("anoMax") String anoMax, Pageable pageable);

    @Query("SELECT a FROM Anuncio a WHERE a.user.id != :userId AND a.marca LIKE %:marca% AND a.modelo LIKE %:modelo% AND a.ano LIKE %:ano% AND a.preco BETWEEN :precoMin AND :precoMax AND (a.ano LIKE %:query% OR a.modelo LIKE %:query% OR a.marca LIKE %:query%)AND a.ano BETWEEN :anoMin AND :anoMax")
    Page<Anuncio> filtroAnunciosExcluindoUsuario(@Param("marca") String marca, @Param("modelo") String modelo, @Param("ano") String ano,
                                 @Param("precoMin") float precoMin, @Param("precoMax") float precoMax, @Param("query") String query, @Param("anoMin") String anoMin, @Param("anoMax") String anoMax, @Param("userId") UUID user_id, Pageable pageable);

    @Query("SELECT a FROM Anuncio a WHERE a.ano LIKE %:query% OR a.modelo LIKE %:query% OR a.marca LIKE %:query%")
    Page<Anuncio> searchAnuncios(@Param("query") String query, Pageable pageable);

    @Query("SELECT a FROM Anuncio a WHERE a.marca LIKE %:marca% AND a.modelo LIKE %:modelo% AND a.ano LIKE %:ano% AND a.preco BETWEEN :precoMin AND :precoMax AND (a.ano LIKE %:query% OR a.modelo LIKE %:query% OR a.marca LIKE %:query%)AND a.ano BETWEEN :anoMin AND :anoMax")
    Page<Anuncio> filtroAnuncios(@Param("marca") String marca, @Param("modelo") String modelo, @Param("ano") String ano,
                                 @Param("precoMin") float precoMin, @Param("precoMax") float precoMax, @Param("query") String query, @Param("anoMin") String anoMin, @Param("anoMax") String anoMax, Pageable pageable);
}
