package com.moveltrack.reactbackend.model;

public enum OrdemDeServicoTipo {

    COBRANCA("Cobrança"),
    INSTALACAO("Instalação Rastreador"), 
    RETIRADA("Retirada de Rastreador"),
    MANUTENCAO("Manutenção de Rastreador");
    
	private String descricao;
	
	public String getDescricao(){
		return descricao;
	}
	
	private OrdemDeServicoTipo(String descricao){
		this.descricao = descricao;
	}
    
    @Override
    public String toString(){
    	return name();
    }
    
}
