package com.moveltrack.reactbackend.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.EscalaTipo;

@RepositoryRestResource(path = "escalaTipos")
public interface EscalaTipoRepository extends PagingAndSortingRepository<EscalaTipo,Integer>,JpaSpecificationExecutor<EscalaTipo> {


}