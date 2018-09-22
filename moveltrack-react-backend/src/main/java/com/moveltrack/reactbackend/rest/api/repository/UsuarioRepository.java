package com.moveltrack.reactbackend.rest.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.moveltrack.reactbackend.model.Pessoa;
import com.moveltrack.reactbackend.model.Usuario;

@RepositoryRestResource(path = "usuarios")
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario,Integer> , JpaSpecificationExecutor<Usuario>{

	Usuario findByNomeUsuario(String username);

}