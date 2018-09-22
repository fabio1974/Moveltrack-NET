package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.Chip;


@RepositoryRestResource(path = "chips")
public interface ChipRepository extends PagingAndSortingRepository<Chip,Integer> , JpaSpecificationExecutor<Chip>  {


}