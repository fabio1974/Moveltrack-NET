package net.moveltrack.financeiro.cnabcaixa.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.util.Utils;


public class TrailerLote extends Registro {

	Integer quantidadeDeRegistrosNoLote;
	Integer quantidadeDeTitulos;
	Double valorTotalDosTitulos;
	private Integer codigoLote;
	
	public TrailerLote(Integer quantidadeDeRegistrosNoLote,Integer quantidadeDeTitulos, Double valorTotalDosTitulos,Integer codigoLote){
		this.quantidadeDeRegistrosNoLote = quantidadeDeRegistrosNoLote;
		this.quantidadeDeTitulos = quantidadeDeTitulos;
		this.valorTotalDosTitulos = valorTotalDosTitulos;
		this.codigoLote = codigoLote;
		setCampos();
	}
	
	

	@Override
	public void setCampos() {
		
		List<Campo> controle = new ArrayList<Campo>();
		controle.add(new Campo(1,3,Constantes.CODIGO_BANCO,"Código do Banco"));
		controle.add(new Campo(4,7,codigoLote,"Código do Lote"));
		controle.add(new Campo(8,8,RegistroTipo.TRAILER_DE_LOTE.getNumero(),"Tipo de Registro"));
		campos.addAll(controle);

		campos.add(new Campo(9,17,"","Filler"));
		
		List<Campo> totais = new ArrayList<Campo>();
		totais.add(new Campo(18,23,this.quantidadeDeRegistrosNoLote,"Quantidade de Registros no Lote"));
		totais.add(new Campo(24,29,this.quantidadeDeTitulos,"Quantidade de Títulos"));
		
		String valor = Utils.priceWithDecimal(valorTotalDosTitulos).replace(".","").replace(",","");
		totais.add(new Campo(30,46,StringUtils.leftPad(valor,17,"0"),"Valor Total dos Títulos"));
		
		totais.add(new Campo(47,69,"0","Cobrança Caucionada"));
		totais.add(new Campo(70,92,"0","Cobrança Descontada"));
		campos.addAll(totais);
		
		campos.add(new Campo(93,240,"","Filler"));
	
	}


	


}
