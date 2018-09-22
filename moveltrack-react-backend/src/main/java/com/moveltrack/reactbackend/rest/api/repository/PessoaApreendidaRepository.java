package com.moveltrack.reactbackend.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.Operacao;
import com.moveltrack.reactbackend.model.PessoaApreendida;


@RepositoryRestResource(path = "pessoaApreendidas")
public interface PessoaApreendidaRepository extends PagingAndSortingRepository<PessoaApreendida,Integer> , JpaSpecificationExecutor<PessoaApreendida>  {

	void deleteByOperacao(Operacao operacao);


}