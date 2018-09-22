package com.moveltrack.reactbackend.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.Crime;
import com.moveltrack.reactbackend.model.Operacao;


@RepositoryRestResource(path = "crimes")
public interface CrimeRepository extends PagingAndSortingRepository<Crime,Integer> , JpaSpecificationExecutor<Crime>  {

	void deleteByOperacao(Operacao operacao);


}