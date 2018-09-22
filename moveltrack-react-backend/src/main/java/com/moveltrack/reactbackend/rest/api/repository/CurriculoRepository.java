package com.moveltrack.reactbackend.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.Curriculo;
import com.moveltrack.reactbackend.model.Operacao;

@RepositoryRestResource(path = "curriculos")
public interface CurriculoRepository extends PagingAndSortingRepository<Curriculo,Integer>, JpaSpecificationExecutor<Curriculo> {

	
}