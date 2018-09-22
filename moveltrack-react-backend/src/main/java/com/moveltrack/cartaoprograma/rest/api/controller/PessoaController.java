package com.moveltrack.cartaoprograma.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.cartaoprograma.model.Pessoa;
import com.moveltrack.cartaoprograma.rest.api.repository.PessoaRepository;
import com.moveltrack.cartaoprograma.rest.get.specification.SpecificationsBuilder;
import com.moveltrack.cartaoprograma.service.PessoaService;

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