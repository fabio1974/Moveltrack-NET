package net.moveltrack.financeiro.cnabcaixa.model;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.domain.MBoleto;
import net.moveltrack.util.Utils;



public class SegmentoR extends Registro {

	private Integer numeroSequencialLote;
	private Integer codigoLote;
	private MBoleto mBoleto;
	
	public SegmentoR(MBoleto mBoleto,Integer codigoLote,Integer numeroSequencialLote) {
		this.mBoleto = mBoleto;
		this.codigoLote = codigoLote;
		this.numeroSequencialLote = numeroSequencialLote;
		setCampos();
	}
	


	@Override
	public void setCampos() {
		
		List<Campo> controle = new ArrayList<Campo>();
		controle.add(new Campo(1,3,Constantes.CODIGO_BANCO,"Código do Banco"));
		controle.add(new Campo(4,7,codigoLote,"Código do Lote"));
		controle.add(new Campo(8,8,RegistroTipo.SEGMENTO.getNumero(),"Tipo de Registro"));
		campos.addAll(controle);
		
		List<Campo> servico = new ArrayList<Campo>();
		servico.add(new Campo(9,13,numeroSequencialLote,"Número Sequencial do Lote - nsl"));
		servico.add(new Campo(14,14,"R","Código do Segmento"));
		servico.add(new Campo(15,15,"","Filler"));
		servico.add(new Campo(16,17,"01","Código de Movimento Remessa"));
		campos.addAll(servico);
		
		List<Campo> desconto2 = new ArrayList<Campo>();
		desconto2.add(new Campo(18,18,"0","Tipo de Desconto"));
		desconto2.add(new Campo(19,26,"0","Data do desconto"));
		desconto2.add(new Campo(27,41,"0","Valor/Percentual"));
		campos.addAll(desconto2);
		
		List<Campo> desconto3 = new ArrayList<Campo>();
		desconto3.add(new Campo(42,42,"0","Tipo de Desconto"));
		desconto3.add(new Campo(43,50,"0","Data do desconto"));
		desconto3.add(new Campo(51,65,"0","Valor/Percentual"));
		campos.addAll(desconto3);
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		
		List<Campo> multa = new ArrayList<Campo>();
		multa.add(new Campo(66,66,"2","Código da Multa - 2: Percentual"));
		multa.add(new Campo(67,74,sdf.format(mBoleto.getDataVencimento()),"Data da Multa"));
		
		String multaStr = Utils.priceWithDecimal(mBoleto.getMulta()).replace(".","").replace(",","");
		multa.add(new Campo(75,89,StringUtils.leftPad(multaStr,15,"0"),"Valor/Percentual"));
		
		campos.addAll(multa);
		
		campos.add(new Campo(90,99,"","Informação ao Pagador"));
		campos.add(new Campo(100,179,mBoleto.getMensagem34()!=null?mBoleto.getMensagem34():"","Mensagem 3 e 4"));
		
		campos.add(new Campo(180,229,mBoleto.getContrato().getCliente().getEmail()!=null?mBoleto.getContrato().getCliente().getEmail():"","Email do Pagador"));
		campos.add(new Campo(230,240,"","Filler"));

	}
}

