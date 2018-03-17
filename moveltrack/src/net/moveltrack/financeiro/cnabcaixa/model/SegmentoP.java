package net.moveltrack.financeiro.cnabcaixa.model;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.domain.MBoleto;
import net.moveltrack.util.Utils;



public class SegmentoP extends Registro {

	private Integer numeroSequencialLote;
	private Integer codigoLote;
	private MBoleto mBoleto;
	
	public SegmentoP(MBoleto mBoleto,Integer codigoLote,Integer numeroSequencialLote) {
		this.mBoleto = mBoleto;
		this.codigoLote = codigoLote;
		this.numeroSequencialLote = numeroSequencialLote;
		setCampos();
	}
	


	@Override
	public void setCampos() {
		
		List<Campo> controle = new ArrayList<Campo>();
		controle.add(new Campo(1,3,Constantes.CODIGO_BANCO,"Código do Banco"));
		controle.add(new Campo(4,7,this.codigoLote,"Código do Lote"));
		controle.add(new Campo(8,8,RegistroTipo.SEGMENTO.getNumero(),"Tipo de Registro"));
		campos.addAll(controle);
		
		List<Campo> servico = new ArrayList<Campo>();
		servico.add(new Campo(9,13,numeroSequencialLote,"Número Sequencial do Lote - nsl"));
		servico.add(new Campo(14,14,"P","Código do Segmento"));
		servico.add(new Campo(15,15,"","Filler"));
		servico.add(new Campo(16,17,"01","Código de Movimento Remessa"));
		campos.addAll(servico);
		
		List<Campo> codIdentificacaoBeneficiario = new ArrayList<Campo>();
		codIdentificacaoBeneficiario.add(new Campo(18,22,Constantes.CODIGO_AGENCIA,"Agência Mantenedora da Conta"));
		codIdentificacaoBeneficiario.add(new Campo(23,23,Constantes.DIGITO_VERIFICADOR_DA_AGENCIA,"Dígito Verificador da Agência"));
		codIdentificacaoBeneficiario.add(new Campo(24,29,Constantes.CODIGO_BENEFICIARIO,"Código do Convênio no Banco"));
		codIdentificacaoBeneficiario.add(new Campo(30,37,"0","Filler"));
		campos.addAll(codIdentificacaoBeneficiario);
		
		campos.add(new Campo(38,40,"0","Filler"));
		
		List<Campo> carteiraNossoNumero = new ArrayList<Campo>();
		carteiraNossoNumero.add(new Campo(41,42,Constantes.CARTEIRA_MODALIDADE_REGISTRADA,"Modalidade da Carteira"));
		carteiraNossoNumero.add(new Campo(43,57,mBoleto.getNossoNumero().substring(2),"Identificação do Título no Banco"));
		campos.addAll(carteiraNossoNumero);

		
		List<Campo> caracteristicaCobranca = new ArrayList<Campo>();
		caracteristicaCobranca.add(new Campo(58,58,1,"Cobranca Simples"));
		caracteristicaCobranca.add(new Campo(59,59,"","Somete preenchido se o boleto for emitido pelo Banco"));
		caracteristicaCobranca.add(new Campo(60,60,2,"Escritural"));
		caracteristicaCobranca.add(new Campo(61,61,2,"Beneficiário Emite"));
		caracteristicaCobranca.add(new Campo(62,62,0,"Postagem pelo Beneficiário"));		
		campos.addAll(caracteristicaCobranca);

		campos.add(new Campo(63,73,mBoleto.getNossoNumero().substring(6),"Seu Número"));
		campos.add(new Campo(74,77,"","Filler"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		
		campos.add(new Campo(78,85,sdf.format(mBoleto.getDataVencimento()),"Vencimento"));
		
		String valor = Utils.priceWithDecimal(mBoleto.getValor()).replace(".","").replace(",","");
		campos.add(new Campo(86,100,StringUtils.leftPad(valor,15,"0"),"Valor"));
		
		campos.add(new Campo(101,105,"0","Ag.Cobradora"));
		campos.add(new Campo(106,106,"0","DV"));
		
		campos.add(new Campo(107,108,"02","Espécie de Título"));
		
		campos.add(new Campo(109,109,"N","Aceite"));
		campos.add(new Campo(110,117,sdf.format(mBoleto.getDataEmissao()),"Emissão"));
		
		
		List<Campo> juros = new ArrayList<Campo>();
		juros.add(new Campo(118,118,"2","Código de Juros"));

		Calendar c = Calendar.getInstance();
		c.setTime(mBoleto.getDataVencimento());
		c.add(Calendar.DAY_OF_YEAR,1);

		juros.add(new Campo(119,126,sdf.format(c.getTime()),"Data de Juros"));
		juros.add(new Campo(127,141,1,"% de juros ao mês"));
		campos.addAll(juros);
		
		List<Campo> desconto = new ArrayList<Campo>();
		desconto.add(new Campo(142,142,"0","Tipo de Desconto")); //Sem desconto
		desconto.add(new Campo(143,150,"0","Data do Desconto")); //Sem desconto
		desconto.add(new Campo(151,165,"0","% de Desconto")); //Sem desconto
		campos.addAll(desconto);
		
		campos.add(new Campo(166,180,"0","IOF"));
		campos.add(new Campo(181,195,"0","Abatimento"));
		campos.add(new Campo(196,206,mBoleto.getNossoNumero().substring(6),"Seu Número"));
		campos.add(new Campo(207,220,"","Filler"));
		
		campos.add(new Campo(221,221,"3","Código Protesto")); //Não protestar
		campos.add(new Campo(222,223,"0","Prazo p/ Protesto")); 
		
		campos.add(new Campo(224,224,"1","Codigo Baixa Devolução"));//Baixar/evolver
		campos.add(new Campo(225,227,"090","Prazo Baixa/Devolução"));//Baixar/Devolver após 90 dias
		
		campos.add(new Campo(228,229,"09","Código da Moeda"));//Real
		
		campos.add(new Campo(230,239,"0","Filler"));
		campos.add(new Campo(240,240,"","Filler"));

	}
}
