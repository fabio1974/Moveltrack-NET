package com.moveltrack.reactbackend.model;


public enum RastreadorTipo {


     ST500	  ("Suntech ST500");


	
	private String descricao;

	private RastreadorTipo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
