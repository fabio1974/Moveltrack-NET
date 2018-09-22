package com.moveltrack.reactbackend.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.reactbackend.model.Curriculo;
import com.moveltrack.reactbackend.model.Operacao;
import com.moveltrack.reactbackend.rest.api.repository.CurriculoRepository;
import com.moveltrack.reactbackend.rest.get.specification.SpecificationsBuilder;
import com.moveltrack.reactbackend.service.OperacaoService;

@BasePathAwareController
public class CurriculoController  {

	@Autowired CurriculoRepository repository;
	@Autowired OperacaoService operacaoService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "curriculos")
    public Page<Curriculo> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {
    	SpecificationsBuilder<Curriculo> ms = new SpecificationsBuilder<Curriculo>();
        return repository.findAll(ms.buildEspecification(search),pageable);
    }
    
    /*@ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "operacaoStatuss")
    public OperacaoStatus[] operacaoStatuss() {
    	return OperacaoStatus.values();
        
    }*/
    
    
/*    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "operacaos")
    public Operacao postOperacao(@RequestBody Operacao operacao) {
    	operacaoService.insert(operacao);
    	return operacao;
    }
    
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "operacaos/{id}")
    public Operacao patchOperacao(@PathVariable(name="id")Integer id, @RequestBody Operacao operacao) {
    	operacaoService.update(operacao);
    	return operacao;
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "operacaos/{id}")
    public void deleteOperacao(@PathVariable(name="id")Integer id) {
    	operacaoService.delete(id);
    }
*/


    
    
}