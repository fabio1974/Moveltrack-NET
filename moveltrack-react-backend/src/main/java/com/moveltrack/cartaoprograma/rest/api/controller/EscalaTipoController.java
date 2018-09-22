package com.moveltrack.cartaoprograma.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.cartaoprograma.model.EscalaTipo;
import com.moveltrack.cartaoprograma.rest.api.repository.EscalaTipoRepository;
import com.moveltrack.cartaoprograma.rest.get.specification.SpecificationsBuilder;

@BasePathAwareController
public class EscalaTipoController  {

	@Autowired EscalaTipoRepository repository;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "escalaTipos")
    public Page<EscalaTipo> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<EscalaTipo> ms = new SpecificationsBuilder<EscalaTipo>();
        return repository.findAll(ms.buildEspecification(search),pageable);
        
    }
}