package com.moveltrack.cartaoprograma.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.cartaoprograma.model.Afastamento;
import com.moveltrack.cartaoprograma.model.AfastamentoTipo;
import com.moveltrack.cartaoprograma.rest.api.repository.AfastamentoRepository;
import com.moveltrack.cartaoprograma.rest.get.specification.SpecificationsBuilder;

@BasePathAwareController
public class AfastamentoController  {

	@Autowired AfastamentoRepository repository;
	

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "afastamentos")
    public Page<Afastamento> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<Afastamento> ms = new SpecificationsBuilder<Afastamento>();
        return repository.findAll(ms.buildEspecification(search),pageable);
        
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "afastamentoTipos")
    public AfastamentoTipo[] afastamentoTipos() {
    	return AfastamentoTipo.values();
        
    }
       
    
}