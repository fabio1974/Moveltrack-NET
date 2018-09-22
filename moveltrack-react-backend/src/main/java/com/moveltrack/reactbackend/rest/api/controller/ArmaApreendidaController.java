package com.moveltrack.reactbackend.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.reactbackend.model.Arma;
import com.moveltrack.reactbackend.model.ArmaApreendida;
import com.moveltrack.reactbackend.rest.api.repository.ArmaApreendidaRepository;
import com.moveltrack.reactbackend.rest.get.specification.SpecificationsBuilder;

@BasePathAwareController
public class ArmaApreendidaController  {

	@Autowired ArmaApreendidaRepository repository;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "armaApreendidas")
    public Page<ArmaApreendida> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<ArmaApreendida> ms = new SpecificationsBuilder<ArmaApreendida>();
        return repository.findAll(ms.buildEspecification(search),pageable);
        
    }
}