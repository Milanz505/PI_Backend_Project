package br.com.projeto.carsoncars.Repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import br.com.projeto.carsoncars.Entities.FIPE.Fipe;

public interface FipeRepository extends CrudRepository<Fipe, UUID> {
}
