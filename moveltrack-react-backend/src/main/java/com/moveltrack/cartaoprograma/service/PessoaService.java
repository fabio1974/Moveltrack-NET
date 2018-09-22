package com.moveltrack.cartaoprograma.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moveltrack.cartaoprograma.model.Pessoa;
import com.moveltrack.cartaoprograma.rest.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired PessoaRepository repository;
	
	@Transactional
	public Page<Pessoa> findAll(Specification<Pessoa> spec, Pageable pageable){
		return repository.findAll(spec,pageable);
		
	}
	

}
