package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.Curriculo;
import com.moveltrack.cartaoprograma.model.Operacao;

@RepositoryRestResource(path = "curriculos")
public interface CurriculoRepository extends PagingAndSortingRepository<Curriculo,Integer>, JpaSpecificationExecutor<Curriculo> {

	
}