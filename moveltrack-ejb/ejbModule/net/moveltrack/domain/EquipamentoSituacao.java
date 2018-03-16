package net.moveltrack.domain;


public enum EquipamentoSituacao {
 
	AGUARDANDO_TESTE("Aguardando teste"),
	ATIVO("Ativo"),
	COM_DEFEITO("Com defeito"),
	EM_TESTE("Em teste"),
	NAO_ENCONTRADO("Não encontrado"),
    NOVO_CONFIGURADO("Novo - Configurado"),
    NOVO_NAO_CONFIGURADO("Novo - Não configurado"),
    OUTRA("Outra"),
    PRONTO_PARA_REUSO("Pronto para reuso"),
    SUSPENSO("Suspenso");
    
 	private String descricao;
	
	public String getDescricao(){
		return descricao;
	}
	
	private EquipamentoSituacao(String descricao){
		this.descricao = descricao;
	}
    
    @Override
    public String toString(){
    	return name();
    }
    
    
}
