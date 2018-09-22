package com.moveltrack.reactbackend.rest.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.AreaOrganizacional;
import com.moveltrack.reactbackend.model.EscalaEquipe;
import com.moveltrack.reactbackend.model.EscalaEquipeSigla;


@RepositoryRestResource(path = "escalaEquipes")
public interface EscalaEquipeRepository extends PagingAndSortingRepository<EscalaEquipe,Integer> , JpaSpecificationExecutor<EscalaEquipe>  {

	void deleteByAreaOrganizacional(AreaOrganizacional areaOrganizacional);

	List<EscalaEquipe> findAllByAreaOrganizacionalAndSigla(AreaOrganizacional areaOrganizacional, EscalaEquipeSigla sigla);
	


}