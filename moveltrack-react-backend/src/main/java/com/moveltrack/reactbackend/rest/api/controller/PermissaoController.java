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

import com.moveltrack.reactbackend.model.Permissao;
import com.moveltrack.reactbackend.rest.api.repository.PermissaoRepository;
import com.moveltrack.reactbackend.rest.get.specification.SpecificationsBuilder;


@BasePathAwareController
public class PermissaoController  {

	@Autowired PermissaoRepository repository;


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "permissaos")
    public Page<Permissao> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<Permissao> ms = new SpecificationsBuilder<Permissao>();
        return repository.findAll(ms.buildEspecification(search),pageable);
        
    }
}