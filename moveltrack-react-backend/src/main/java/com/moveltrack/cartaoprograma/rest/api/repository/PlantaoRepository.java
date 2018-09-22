package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.Arma;
import com.moveltrack.cartaoprograma.model.Escala;
import com.moveltrack.cartaoprograma.model.Plantao;

@RepositoryRestResource(path = "plantaos")
public interface PlantaoRepository extends PagingAndSortingRepository<Plantao,Integer>,JpaSpecificationExecutor<Plantao> {

	void deleteAllByEscala(Escala escala);


}