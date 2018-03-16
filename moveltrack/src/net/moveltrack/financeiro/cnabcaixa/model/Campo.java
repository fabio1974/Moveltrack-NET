package net.moveltrack.financeiro.cnabcaixa.model;

import java.text.Normalizer;

import org.apache.commons.lang3.StringUtils;

public class Campo {
	
	private String descricao;
	private Object conteudo;
	private int inicio;
	private int  fim;
	
	
	
	public Campo(int inicio, int fim,Object conteudo,String descricao) {
		super();
		this.descricao = descricao;
		this.conteudo = conteudo;
		this.inicio = inicio;
		this.fim = fim;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Object getConteudo() {
		return conteudo;
	}
	public void setConteudo(Object conteudo) {
		this.conteudo = conteudo;
	}
	public int getInicio() {
		return inicio;
	}
	public void setInicio(int inicio) {
		this.inicio = inicio;
	}
	public int getFim() {
		return fim;
	}
	public void setFim(int fim) {
		this.fim = fim;
	}
	public String getConteudoFormatado() {

		if(conteudo instanceof String){
			if(conteudo.toString().equals("0"))
				return StringUtils.leftPad(conteudo.toString(),fim-inicio+1,"0");
			else if(StringUtils.isBlank(conteudo.toString()))
				return StringUtils.leftPad(conteudo.toString(),fim-inicio+1," ");
			else{
				String conteudoStr = Normalizer.normalize(conteudo.toString(),Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]","");
				if(conteudoStr.length() <= fim-inicio+1)
					return StringUtils.rightPad(conteudoStr,fim-inicio+1," ");
				else
					return conteudoStr.substring(0,fim-inicio+1);
			}
		}else if(conteudo instanceof Integer){
			String aux = Integer.toString((Integer)conteudo);
			return StringUtils.leftPad(aux,fim-inicio+1,"0");
		}
		
		return null;
	}
}
