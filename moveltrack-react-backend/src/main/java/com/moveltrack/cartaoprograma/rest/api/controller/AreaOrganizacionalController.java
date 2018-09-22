package com.moveltrack.cartaoprograma.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.cartaoprograma.model.AreaOrganizacional;
import com.moveltrack.cartaoprograma.rest.api.repository.AreaOrganizacionalRepository;
import com.moveltrack.cartaoprograma.rest.get.specification.SpecificationsBuilder;

@BasePathAwareController
public class AreaOrganizacionalController  {

	@Autowired AreaOrganizacionalRepository repository;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "areaOrganizacionals")
    public Page<AreaOrganizacional> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<AreaOrganizacional> ms = new SpecificationsBuilder<AreaOrganizacional>();
        return repository.findAll(ms.buildEspecification(search),pageable);
        
    }
}