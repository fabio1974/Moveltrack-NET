package com.moveltrack.reactbackend.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.Veiculo;


@RepositoryRestResource(path = "veiculos")
public interface VeiculoRepository extends PagingAndSortingRepository<Veiculo,Integer> , JpaSpecificationExecutor<Veiculo>  {


}