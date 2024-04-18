package br.com.projeto.carsoncars.Repository;

import java.util.List;
import java.util.Optional;
//import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import br.com.projeto.carsoncars.Entities.User.User;

@Repository
public interface Repositorio extends CrudRepository<User, UUID> {
    List<User> findAll();
    Optional<User> findById(UUID id); // Renomeando o método para evitar conflito
    User findByEmail(String email);
    // BUSCA DE USER BEM LEGAL
    List<User> findByNomeContaining(String term);
    List<User> findByNomeStartsWith(String term);
    List<User> findByNomeEndsWith(String term);


}
