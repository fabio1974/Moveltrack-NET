package com.moveltrack.cartaoprograma.rest.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.cartaoprograma.model.AreaOrganizacional;
import com.moveltrack.cartaoprograma.model.EscalaEquipe;
import com.moveltrack.cartaoprograma.model.EscalaEquipeSigla;


@RepositoryRestResource(path = "escalaEquipes")
public interface EscalaEquipeRepository extends PagingAndSortingRepository<EscalaEquipe,Integer> , JpaSpecificationExecutor<EscalaEquipe>  {

	void deleteByAreaOrganizacional(AreaOrganizacional areaOrganizacional);

	List<EscalaEquipe> findAllByAreaOrganizacionalAndSigla(AreaOrganizacional areaOrganizacional, EscalaEquipeSigla sigla);
	


}