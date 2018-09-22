package com.moveltrack.reactbackend.model;

public enum DespesaTipo {
 
    ACESSORIOS("Acessórios"),
    AGUA("Água"),
    ALUGUEL("Aluguel"),
    ANUNCIOS("Anúncios"),
    COMISSAO_VENDEDOR("Comissão Vendedor"),
    COMPRA_DE_CHIP("Compra Chip"), 
    RECARGA_DE_CHIP("Recarga de Chip"), 
    COMPRA_DE_RASTREADOR("Compra Rastreador"),
    CORREIOS("Correios"),
    DOMINIOS("Domínios"),    
    HOSPEDAGEM_SITE("Hospedagem de Site"),
    IMOBILIZADO("Imobilizado"),
    INTERNET("Internet"),
    LUZ("Luz"),
    OUTROS("Outros"),
    PROLABORE("Pró-labore"),
    SALARIOS("Salários e Adicionais"),
    SERVICOS_DO_INSTALADOR("Serviços do Instalador"),
    TRIBUTOS("Tributos");

    
	private String descricao;
	
	public String getDescricao(){
		return descricao;
	}
	
	private DespesaTipo(String descricao){
		this.descricao = descricao;
	}
    
    @Override
    public String toString(){
    	return name();
    }
    
}
