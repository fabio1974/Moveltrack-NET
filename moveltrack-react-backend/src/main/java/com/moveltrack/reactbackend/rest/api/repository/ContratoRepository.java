package com.moveltrack.reactbackend.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.Cliente;
import com.moveltrack.reactbackend.model.Contrato;


@RepositoryRestResource(path = "contratos")
public interface ContratoRepository extends PagingAndSortingRepository<Contrato,Integer> , JpaSpecificationExecutor<Contrato>  {

	Contrato findByCliente(Cliente cliente);


}