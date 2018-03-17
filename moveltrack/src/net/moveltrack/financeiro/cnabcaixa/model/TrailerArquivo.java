package net.moveltrack.financeiro.cnabcaixa.model;

import java.util.ArrayList;
import java.util.List;

public class TrailerArquivo extends Registro {

	Integer quantidadeDeLotes;
	Integer quantidadeDeRegistrosDoArquivo;
	
	public TrailerArquivo(Integer quantidadeDeLotes,Integer quantidadeDeRegistrosDoArquivo){
		this.quantidadeDeLotes = quantidadeDeLotes;
		this.quantidadeDeRegistrosDoArquivo = quantidadeDeRegistrosDoArquivo;
		setCampos();
	}
	
	

	@Override
	public void setCampos() {
		
		List<Campo> controle = new ArrayList<Campo>();
		controle.add(new Campo(1,3,Constantes.CODIGO_BANCO,"Código do Banco"));
		controle.add(new Campo(4,7,"9999","Código do Lote"));
		controle.add(new Campo(8,8,RegistroTipo.TRAILER_DE_ARQUIVO.getNumero(),"Tipo de Registro"));
		campos.addAll(controle);

		campos.add(new Campo(9,17,"","Filler"));
		
		List<Campo> totais = new ArrayList<Campo>();
		totais.add(new Campo(18,23,this.quantidadeDeLotes,"Quantidade de Lotes do Arquivo"));
		totais.add(new Campo(24,29,this.quantidadeDeRegistrosDoArquivo,"Quantidade de Registros do Arquivo"));
		campos.addAll(totais);
		
		campos.add(new Campo(30,35,"","Filler"));
		campos.add(new Campo(36,240,"","Filler"));
		
		
	}


	


}
