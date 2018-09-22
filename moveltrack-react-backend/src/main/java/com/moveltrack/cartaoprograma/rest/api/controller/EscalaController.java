package com.moveltrack.cartaoprograma.rest.api.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.moveltrack.cartaoprograma.model.Escala;
import com.moveltrack.cartaoprograma.rest.api.repository.EscalaRepository;
import com.moveltrack.cartaoprograma.rest.get.specification.SpecificationsBuilder;
import com.moveltrack.cartaoprograma.service.EscalaService;



@BasePathAwareController
public class EscalaController  {

	@Autowired EscalaRepository repository;
	@Autowired EscalaService service;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "escalas")
    public Page<Escala> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<Escala> ms = new SpecificationsBuilder<Escala>();
        return repository.findAll(ms.buildEspecification(search),pageable);
    }
    
    

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "escalas")
    public void post(@RequestBody Escala escala, HttpServletRequest req, HttpServletResponse resp) {
    	service.saveEscala(escala);
    }
    
    

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "escalas/{id}")
    public void delete(@PathVariable(name="id")Integer id) {
    	Optional<Escala> escala = repository.findById(id);
		if (escala.isPresent()) 
			service.deleteEscala(escala.get());
    }

    
    
    
}