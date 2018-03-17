package net.moveltrack.financeiro.cnabcaixa.model;

public enum CodigoMovimento {
	
	ENTRADA_DE_TITULO (1,"Entrada de Título"), 
	PEDIDO_DE_BAIXA(2,"Pedido de Baixa");

	private final Integer numero;
	private final String descricao;
	
	CodigoMovimento(int numero,String descricao) {
		this.numero = numero;
		this.descricao = descricao;
	}

	public Integer getNumero() {
		
		return numero;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
	
	
}
