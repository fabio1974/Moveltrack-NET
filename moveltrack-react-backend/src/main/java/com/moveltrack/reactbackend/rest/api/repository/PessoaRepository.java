package com.moveltrack.reactbackend.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.Pessoa;

@RepositoryRestResource(path = "pessoas")
public interface PessoaRepository extends PagingAndSortingRepository<Pessoa,Integer> , JpaSpecificationExecutor<Pessoa>{

	Pessoa findByCpf(String username);

	/*List<Pessoa> findByNome(@Param("nome") String nome);
	Pessoa findBySenha(@Param("senha") String senha);
	Pessoa findByMatricula(@Param("matricula") int matricula);
	Pessoa findByEmail(String email);
	Pessoa findByCpf(String username);*/
	

}