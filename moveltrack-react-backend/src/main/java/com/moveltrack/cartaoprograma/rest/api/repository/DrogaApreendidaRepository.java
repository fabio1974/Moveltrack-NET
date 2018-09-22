package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.DrogaApreendida;
import com.moveltrack.cartaoprograma.model.Operacao;


@RepositoryRestResource(path = "drogaApreendidas")
public interface DrogaApreendidaRepository extends PagingAndSortingRepository<DrogaApreendida,Integer> , JpaSpecificationExecutor<DrogaApreendida>  {

	void deleteByOperacao(Operacao operacao);


}