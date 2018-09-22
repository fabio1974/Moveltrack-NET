package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.Rastreador;


@RepositoryRestResource(path = "rastreadors")
public interface RastreadorRepository extends PagingAndSortingRepository<Rastreador,Integer> , JpaSpecificationExecutor<Rastreador>  {


}