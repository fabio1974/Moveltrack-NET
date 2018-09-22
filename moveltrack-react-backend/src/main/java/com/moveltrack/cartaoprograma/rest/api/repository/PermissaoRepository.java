package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.Arma;
import com.moveltrack.cartaoprograma.model.Permissao;

@RepositoryRestResource(path = "permissaos")
public interface PermissaoRepository extends PagingAndSortingRepository<Permissao,Integer>, JpaSpecificationExecutor<Permissao> {


}