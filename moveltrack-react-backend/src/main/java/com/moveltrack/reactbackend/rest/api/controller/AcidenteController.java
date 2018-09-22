package com.moveltrack.reactbackend.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.reactbackend.model.Acidente;
import com.moveltrack.reactbackend.rest.api.repository.AcidenteRepository;
import com.moveltrack.reactbackend.rest.get.specification.SpecificationsBuilder;

@BasePathAwareController
public class AcidenteController  {

	@Autowired AcidenteRepository repository;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "acidentes")
    public Page<Acidente> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<Acidente> ms = new SpecificationsBuilder<Acidente>();
        return repository.findAll(ms.buildEspecification(search),pageable);
        
    }
}