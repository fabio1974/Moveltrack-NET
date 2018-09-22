package com.moveltrack.reactbackend.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.ArmaApreendida;
import com.moveltrack.reactbackend.model.Operacao;


@RepositoryRestResource(path = "armaApreendidas")
public interface ArmaApreendidaRepository extends PagingAndSortingRepository<ArmaApreendida,Integer> , JpaSpecificationExecutor<ArmaApreendida>  {

	void deleteByOperacao(Operacao operacao);


}