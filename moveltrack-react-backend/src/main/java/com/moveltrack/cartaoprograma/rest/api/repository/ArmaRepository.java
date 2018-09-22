package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.Arma;


@RepositoryRestResource(path = "armas")
public interface ArmaRepository extends PagingAndSortingRepository<Arma,Integer> , JpaSpecificationExecutor<Arma>  {


}