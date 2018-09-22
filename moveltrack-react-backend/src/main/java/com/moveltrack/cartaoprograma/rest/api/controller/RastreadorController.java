package com.moveltrack.cartaoprograma.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.cartaoprograma.model.Rastreador;
import com.moveltrack.cartaoprograma.model.RastreadorTipo;
import com.moveltrack.cartaoprograma.rest.api.repository.RastreadorRepository;
import com.moveltrack.cartaoprograma.rest.get.specification.SpecificationsBuilder;

@BasePathAwareController
public class RastreadorController  {

	@Autowired RastreadorRepository repository;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "rastreadors")
    public Page<Rastreador> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {
    	SpecificationsBuilder<Rastreador> ms = new SpecificationsBuilder<Rastreador>();
        return repository.findAll(ms.buildEspecification(search),pageable);
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "rastreadorTipos")
    public RastreadorTipo[] operadoras() {
    	return RastreadorTipo.values();
        
    }
    
}