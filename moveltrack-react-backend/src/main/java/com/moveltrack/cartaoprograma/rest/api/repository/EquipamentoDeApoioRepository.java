package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.EquipamentoDeApoio;
import com.moveltrack.cartaoprograma.model.EquipamentoDeApoioID;


@RepositoryRestResource(path = "equipamentoDeApoios")
public interface EquipamentoDeApoioRepository extends PagingAndSortingRepository<EquipamentoDeApoio,EquipamentoDeApoioID>, JpaSpecificationExecutor<EquipamentoDeApoio> {

	void deleteByOperacaoId(int id);
	
	
}