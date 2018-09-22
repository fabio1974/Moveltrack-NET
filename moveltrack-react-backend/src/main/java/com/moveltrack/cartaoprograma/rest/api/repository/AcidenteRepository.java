package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.Acidente;
import com.moveltrack.cartaoprograma.model.Operacao;


@RepositoryRestResource(path = "acidentes")
public interface AcidenteRepository extends PagingAndSortingRepository<Acidente,Integer> , JpaSpecificationExecutor<Acidente>  {

	void deleteByOperacao(Operacao operacao);


}