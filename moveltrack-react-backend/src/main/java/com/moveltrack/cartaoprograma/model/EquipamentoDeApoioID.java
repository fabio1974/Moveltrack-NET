package com.moveltrack.cartaoprograma.model;

import java.io.Serializable;

public class EquipamentoDeApoioID implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String descricao;
	private Integer operacaoId;

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getOperacaoId() {
		return operacaoId;
	}
	public void setOperacaoId(Integer operacaoId) {
		this.operacaoId = operacaoId;
	}
	
	

}

