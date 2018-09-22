package com.moveltrack.reactbackend.model;


public enum VeiculoTipo {

    MOTOCICLETA("MOTOCICLETA"),
    AUTOMOVEL("AUTOMÓVEL"),
    CAMINHAO("CAMINHÃO"),
    TRATOR("TRATOR"),
    PICKUP("PICKUP");
	
	private String descricao;
	
	public String getDescricao(){
		return descricao;
	}
	
	private VeiculoTipo(String descricao){
		this.descricao = descricao;
	}
    
    @Override
    public String toString(){
    	return name();
    }
	
}
