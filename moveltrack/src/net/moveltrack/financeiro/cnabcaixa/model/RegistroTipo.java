package net.moveltrack.financeiro.cnabcaixa.model;

public 	enum RegistroTipo {
	
	
	HEADER_DE_ARQUIVO(0,"Header de Arquivo"), 
	HEADER_DE_LOTE(1,"Header de Lote"),
	SEGMENTO(3, "Segmento"),
	TRAILER_DE_LOTE(5,"Trailer de lote"),
	TRAILER_DE_ARQUIVO(9,"Trailer de Arquivo");
	
	private Integer numero;
	private String descricao;

	
	RegistroTipo(int numero,String descricao) {
		this.numero = numero;
		this.descricao = descricao;
	}


	public Integer getNumero() {
		return numero;
	}


	public void setNumero(Integer numero) {
		this.numero = numero;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
	
}
