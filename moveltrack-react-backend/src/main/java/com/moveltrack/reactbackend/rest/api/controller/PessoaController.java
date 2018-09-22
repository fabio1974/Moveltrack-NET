package com.moveltrack.reactbackend.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.reactbackend.model.Pessoa;
import com.moveltrack.reactbackend.rest.api.repository.PessoaRepository;
import com.moveltrack.reactbackend.rest.get.specification.SpecificationsBuilder;
import com.moveltrack.reactbackend.service.PessoaService;

@BasePathAwareController
public class PessoaController  {

	@Autowired PessoaRepository repository;


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "pessoas")
    public Page<Pessoa> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<Pessoa> ms = new SpecificationsBuilder<Pessoa>();
        return repository.findAll(ms.buildEspecification(search),pageable);
        
    }
}