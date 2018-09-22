package com.moveltrack.reactbackend.rest.api.controller;

import org.springframework.data.rest.webmvc.BasePathAwareController;

@BasePathAwareController
public class OperacaoController  {


	//@Autowired OperacaoService operacaoService;
/*
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "operacaos")
    public Page<Operacao> search(@RequestParam(value = "search",required=false) String search,Pageable pageable) {

    	SpecificationsBuilder<Operacao> ms = new SpecificationsBuilder<Operacao>();
        return repository.findAll(ms.buildEspecification(search),pageable);
        
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "operacaoStatuss")
    public OperacaoStatus[] operacaoStatuss() {
    	return OperacaoStatus.values();
        
    }
    
    @ResponseBody
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