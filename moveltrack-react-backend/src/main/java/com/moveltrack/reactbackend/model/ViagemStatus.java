package com.moveltrack.reactbackend.model;

public enum ViagemStatus {
 
    ABERTA("ABERTA"),
    PARTIDA_EM_ATRASO("PARTIDA EM ATRASO"),
    SAINDO("SAINDO"),
    SAIU_DA_CERCA("SAIU DA CERCA"), 
    NA_ESTRADA("NA ESTRADA"), 
    ENTROU_NA_CERCA("ENTROU NA CERCA"),
    SE_APROXIMANDO("SE APROXIMANDO"),
    CHEGADA_EM_TEMPO("CHEGADA EM TEMPO"),
    CHEGADA_EM_ATRASO("CHEGADA EM ATRASO"),
    ENCERRADA("ENCERRADA");

    
	private String descricao;
	
	public String getDescricao(){
		return descricao;
	}
	
	private ViagemStatus(String descricao){
		this.descricao = descricao;
	}
    
    @Override
    public String toString(){
    	return name();
    }
    
}
