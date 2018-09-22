package com.moveltrack.cartaoprograma.rest.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.moveltrack.cartaoprograma.model.EscalaEquipe;
import com.moveltrack.cartaoprograma.rest.api.repository.EscalaEquipeRepository;
import com.moveltrack.cartaoprograma.rest.get.specification.SpecificationsBuilder;
import com.moveltrack.cartaoprograma.service.EscalaEquipeService;



@BasePathAwareController
public class EscalaEquipeController  {

	@Autowired EscalaEquipeRepository repository;
	@Autowired EscalaEquipeService service;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "escalaEquipes")
    public Page<EscalaEquipe> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<EscalaEquipe> ms = new SpecificationsBuilder<EscalaEquipe>();
        return repository.findAll(ms.buildEspecification(search),pageable);
    }
    
    

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "escalaEquipes")
    public void postOperacao(@RequestBody List<EscalaEquipe> escalaEquipes, HttpServletRequest req, HttpServletResponse resp) {
    	service.saveEscalaEquipe(escalaEquipes);
    }
    
    
    
}