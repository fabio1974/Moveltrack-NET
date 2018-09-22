package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.Arma;
import com.moveltrack.cartaoprograma.model.Perfil;

@RepositoryRestResource(path = "perfils")
public interface PerfilRepository extends PagingAndSortingRepository<Perfil,Integer>, JpaSpecificationExecutor<Perfil> {

	
}