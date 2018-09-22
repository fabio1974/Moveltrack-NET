package com.moveltrack.reactbackend.model;

public enum DespesaFrotaEspecie {
 
	//Veículos
	COMBUSTIVEL("COMBUSTÍVEL"),
    ESTIVA("SERV. DE ESTIVA"),
    DIARIA("DIÁRIA"),
    IPVA("IPVA"),
    LICENCIAMENTO("LICENCIAMENTO"),
    MANUTENCAO("MANUTENÇÃO - PEÇAS E SERVIÇOS"),
    MULTA_DE_TRANSITO("MULTA DE TRANSITO"),
    OUTROS("OUTROS"),
    SEGURO_OBRIGATORIO("SEGURO OBRIGATORIO"),
    TRABALHISTAS("SALARIOS E ADICIONAIS TRAB.");

    
   
	private String descricao;
	
	public String getDescricao(){
		return descricao;
	}
	
	private DespesaFrotaEspecie(String descricao){
		this.descricao = descricao;
	}
    
    @Override
    public String toString(){
    	return name();
    }
    
}
