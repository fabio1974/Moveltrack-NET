package net.moveltrack.financeiro.cnabcaixa.model;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HeaderLote extends Registro {

	private Integer numeroSequenciadoArquivo;
	private Integer codigoLote;
	private Date dataGravacao;
	
	public HeaderLote(Integer codigoLote,Integer numeroSequenciadoArquivo,Date dataGravacao) {
		this.numeroSequenciadoArquivo = numeroSequenciadoArquivo;
		this.codigoLote = codigoLote;
		this.dataGravacao = dataGravacao;
		setCampos();
	}
	


	@Override
	public void setCampos() {
		
		
		List<Campo> controle = new ArrayList<Campo>();
		controle.add(new Campo(1,3,Constantes.CODIGO_BANCO,"Código do Banco"));
		controle.add(new Campo(4,7,codigoLote,"Código do Lote"));
		controle.add(new Campo(8,8,RegistroTipo.HEADER_DE_LOTE.getNumero(),"Tipo de Registro"));
		campos.addAll(controle);
		
		
		List<Campo> servico = new ArrayList<Campo>();
		servico.add(new Campo(9,9,Constantes.OPERACAO_REMESSA,"Tipo de Operação"));
		servico.add(new Campo(10,11,Constantes.COBRANCA_REGISTRADA,"Tipo de Serviço"));
		servico.add(new Campo(12,13,"0","Filler"));
		servico.add(new Campo(14,16,Constantes.LOTE_LAYOUT_VERSION,"Tipo de Registro"));
		campos.addAll(servico);
		
		campos.add(new Campo(17,17,"","Filler"));

		
		List<Campo> empresa = new ArrayList<Campo>();
		empresa.add(new Campo(18,18,Constantes.CNPJ,"Tipo de Inscrição do Beneficiário"));
		empresa.add(new Campo(19,33,"0"+Constantes.CNPJ_MOVELTRACK,"Número de Inscrição do Beneficiário"));
		empresa.add(new Campo(34,39,Constantes.CODIGO_BENEFICIARIO,"Código do Beneficiário"));
		empresa.add(new Campo(40,53,"0","Uso Exclusivo CAIXA"));
		empresa.add(new Campo(54,58,Constantes.CODIGO_AGENCIA,"Agência Mantenedora da Conta"));
		empresa.add(new Campo(59,59,Constantes.DIGITO_VERIFICADOR_DA_AGENCIA,"Dígito Verificador da Agência"));
		empresa.add(new Campo(60,65,Constantes.CODIGO_BENEFICIARIO,"Código do Convênio no Banco"));
		empresa.add(new Campo(66,72,"0","Código do Modelo de Boleto Personalizado"));
		empresa.add(new Campo(73,73,"0","Filler"));
		empresa.add(new Campo(74,103,Constantes.RAZAO_SOCIAL,"Nome da Empresa"));
		campos.addAll(empresa);
		
		List<Campo> informacoes = new ArrayList<Campo>();
		informacoes.add(new Campo(104,183,"Pagável em qualquer agencia bancária ou lotérica até o vencimento","Mensagem 1 e 2"));
		campos.addAll(informacoes);
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

		List<Campo> controleDaCobranca = new ArrayList<Campo>();
		controleDaCobranca.add(new Campo(184,191,numeroSequenciadoArquivo,"Número da Remessa"));
		controleDaCobranca.add(new Campo(192,199,sdf.format(dataGravacao),"Data de Gravação"));
		campos.addAll(controleDaCobranca);

		campos.add(new Campo(200,207,"0","Filler"));
		campos.add(new Campo(208,240,"","Filler"));
	}










	
	
}
