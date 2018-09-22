package com.moveltrack.reactbackend.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.reactbackend.model.DrogaApreendida;
import com.moveltrack.reactbackend.model.DrogaApreendidaTipo;
import com.moveltrack.reactbackend.model.DrogaApreendidaUnidade;
import com.moveltrack.reactbackend.rest.api.repository.DrogaApreendidaRepository;
import com.moveltrack.reactbackend.rest.get.specification.SpecificationsBuilder;

@BasePathAwareController
public class DrogaApreendidaController  {

	@Autowired DrogaApreendidaRepository repository;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "drogaApreendidas")
    public Page<DrogaApreendida> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<DrogaApreendida> ms = new SpecificationsBuilder<DrogaApreendida>();
        return repository.findAll(ms.buildEspecification(search),pageable);
        
    }
    
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "drogaApreendidaUnidades")
    public DrogaApreendidaUnidade[] drogaApreendidaUnidades() {
    	return DrogaApreendidaUnidade.values();
        
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "drogaApreendidaTipos")
    public DrogaApreendidaTipo[] drogaApreendidaTipos() {
    	return DrogaApreendidaTipo.values();
        
    }
    
}