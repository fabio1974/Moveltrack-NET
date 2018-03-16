package net.moveltrack.financeiro.cnabcaixa.model;

import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;





public abstract class Registro {

	public List<Campo> campos;
	
	public Registro() {
		this.campos = new ArrayList<Campo>();
	}

	public abstract void setCampos();

	public String printRegistro(PrintWriter writer) {
		StringBuffer lingueta = new StringBuffer("");
		
		System.out.println("Tipo do Registro:"+ this.getClass().getTypeName() +"------------------------------------------");
		
		for (Campo campo : campos) {
			String conteudo = campo.getConteudoFormatado();
			int tamanhoOficial = campo.getFim()-campo.getInicio()+1;
			int tamanhoReal = conteudo.length(); 
			if(tamanhoReal==tamanhoOficial)
				System.out.println("ok-"+campo.getDescricao());
			else
				System.out.println(campo.getDescricao()+" COM PROBLEMA!");
			lingueta.append(conteudo);
		}
		
		System.out.println("Tamanho do Registro:"+lingueta.length()+"---- Tipo:"+ this.getClass().getTypeName() +"------------------------------------------");
		System.out.println();
		
		writer.println(lingueta.toString());
		return lingueta.toString();
	}
}
