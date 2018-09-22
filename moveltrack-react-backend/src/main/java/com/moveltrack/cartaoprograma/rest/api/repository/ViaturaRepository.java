package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.Viatura;


@RepositoryRestResource(path = "viaturas")
public interface ViaturaRepository extends PagingAndSortingRepository<Viatura,Integer> , JpaSpecificationExecutor<Viatura>  {

	Viatura findByPlaca(String placa);


}