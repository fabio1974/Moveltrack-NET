package com.moveltrack.reactbackend.model;

public enum DespesaFrotaCategoria {

    MOTORISTA("MOTORISTA"),
    VEICULO("VEÍCULO"),
    VIAGEM("VIAGEM");
    
	private String descricao;
	
	public String getDescricao(){
		return descricao;
	}
	
	private DespesaFrotaCategoria(String descricao){
		this.descricao = descricao;
	}
    
    @Override
    public String toString(){
    	return name();
    }
    
}
