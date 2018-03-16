package net.moveltrack.domain;


public enum Cor {
	AMARELA("AMARELA"), 
	AZUL("AZUL"), 
    AZUL_METALICA("AZUL METÁLICA"),
    BEGE("BEGE"),
    BRANCA("BRANCA"),
    CHUMBO("CHUMBO"),    
    CINZA("CINZA"),
    LARANJA("LARANJA"), 
    PRATA("PRATA"), 
    PRETA("PRETA"),
    ROSA("ROSA"),
    ROSA_METALICA("ROSA METÁLICA"),    
    VERDE("VERDE"),    
    VERMELHA("VERMELHA"), 
    VERMELHA_METALICA("VERMELHA METÁLICA");
	
	private String descricao;
	
	public String getDescricao(){
		return descricao;
	}
	
	private Cor(String descricao){
		this.descricao = descricao;
	}
    
    @Override
    public String toString(){
    	return name();
    }
	
}
