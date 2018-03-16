package net.moveltrack.financeiro.cnabcaixa.model;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.PerfilTipo;



public class SegmentoQ extends Registro {

	private Integer numeroSequencialLote;
	private Integer codigoLote;
	private MBoleto mBoleto;
	
	public SegmentoQ(MBoleto mBoleto,Integer codigoLote,Integer numeroSequencialLote) {
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
		servico.add(new Campo(14,14,"Q","Código do Segmento"));
		servico.add(new Campo(15,15,"","Filler"));
		servico.add(new Campo(16,17,"01","Código de Movimento Remessa"));
		campos.addAll(servico);
		
		boolean pf = mBoleto.getContrato().getCliente().getUsuario().getPerfil().getTipo() == PerfilTipo.CLIENTE_PF;
		
		List<Campo> pagador = new ArrayList<Campo>();
		pagador.add(new Campo(18,18,pf?"1":"2","Tipo de Inscricao"));
		
		String cpfCnpj = pf?mBoleto.getContrato().getCliente().getCpf():mBoleto.getContrato().getCliente().getCnpj();
		
		pagador.add(new Campo(19,33,StringUtils.leftPad(cpfCnpj.replace("-","").replace(".","").replace("/","").trim(),15,"0"),"Número de Inscrição CPF/CNPJ"));
		pagador.add(new Campo(34,73,mBoleto.getContrato().getCliente().getNome(),"Nome do pagador"));
		
		String endereco = mBoleto.getContrato().getCliente().getEndereco()+","
						+mBoleto.getContrato().getCliente().getNumero()+
						(mBoleto.getContrato().getCliente().getComplemento()!=null?mBoleto.getContrato().getCliente().getComplemento():"");
		
		pagador.add(new Campo(74,113,endereco,"Endereço do pagador"));
		pagador.add(new Campo(114,128,mBoleto.getContrato().getCliente().getBairro(),"Bairro do pagador"));
		pagador.add(new Campo(129,136,mBoleto.getContrato().getCliente().getCep().replace("-","").replace(".","").trim(),"Cep do pagador"));
		pagador.add(new Campo(137,151,mBoleto.getContrato().getCliente().getMunicipio().getDescricao(),"Cidade do Pagador"));
		pagador.add(new Campo(152,153,mBoleto.getContrato().getCliente().getMunicipio().getUf().getSigla(),"Estado do Pagador"));
		campos.addAll(pagador);
		
		campos.add(new Campo(154,169,"0","Dados do Avalista - Inscrição"));
		campos.add(new Campo(170,209,"","Dados do Avalista-Nome"));
		
		campos.add(new Campo(210,212,"0","Banco Correspondente"));
		campos.add(new Campo(213,232,"","Banco Corespondente - Nosso Numero"));
		campos.add(new Campo(233,240,"","Filler"));

	}
}
