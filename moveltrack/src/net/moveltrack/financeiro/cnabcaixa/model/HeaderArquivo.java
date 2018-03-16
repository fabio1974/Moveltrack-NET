package net.moveltrack.financeiro.cnabcaixa.model;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HeaderArquivo extends Registro {

	private Integer nsa;
	private Date dataGravacao;
	

	public HeaderArquivo(Integer nsa, Date dataGravacao) {
		this.nsa = nsa;
		this.dataGravacao = dataGravacao;
		setCampos();
	}

	@Override
	public void setCampos() {
		
		SimpleDateFormat ddMMyyyy = new SimpleDateFormat("ddMMyyyy");
		SimpleDateFormat HHmmss = new SimpleDateFormat("HHmmss");
		
		List<Campo> controle = new ArrayList<Campo>();
		controle.add(new Campo(1,3,Constantes.CODIGO_BANCO,"Código do Banco"));
		controle.add(new Campo(4,7,"0000","Código do Lote"));
		controle.add(new Campo(8,8,RegistroTipo.HEADER_DE_ARQUIVO.getNumero(),"Tipo de Registro"));
		campos.addAll(controle);

		campos.add(new Campo(9,17,"","Filler"));
		
		List<Campo> empresaBeneficiaria = new ArrayList<Campo>();
		empresaBeneficiaria.add(new Campo(18,18,Constantes.CNPJ,"Tipo de Inscrição do Beneficiário"));
		empresaBeneficiaria.add(new Campo(19,32,Constantes.CNPJ_MOVELTRACK,"Número de Inscrição do Beneficiário"));
		empresaBeneficiaria.add(new Campo(33,52,"0","Uso Exclusivo CAIXA"));
		empresaBeneficiaria.add(new Campo(53,57,Constantes.CODIGO_AGENCIA,"Agência Mantenedora da Conta"));
		empresaBeneficiaria.add(new Campo(58,58,Constantes.DIGITO_VERIFICADOR_DA_AGENCIA,"Dígito Verificador da Agência"));
		empresaBeneficiaria.add(new Campo(59,64,Constantes.CODIGO_BENEFICIARIO,"Código do Beneficiário"));
		empresaBeneficiaria.add(new Campo(65,72,"0","Uso Exclusivo CAIXA"));
		empresaBeneficiaria.add(new Campo(73,102,Constantes.RAZAO_SOCIAL,"Nome da Empresa"));
		campos.addAll(empresaBeneficiaria);
		
		campos.add(new Campo(103,132,Constantes.NOME_DO_BANCO,"Nome do Banco"));
		campos.add(new Campo(133,142,"","Filler"));
		
		List<Campo> arquivo = new ArrayList<Campo>();
		arquivo.add(new Campo(143,143,1,"Código Remessa /Retorno"));
		arquivo.add(new Campo(144,151,ddMMyyyy.format(dataGravacao),"Data de Geração do Arquivo - DDMMAAAA"));
		arquivo.add(new Campo(152,157,HHmmss.format(dataGravacao),"Hora de Geração do Arquivo - HHMMSS"));
		arquivo.add(new Campo(158,163,nsa,"Número da Remessa"));
		arquivo.add(new Campo(164,166,Constantes.LAYOUT_VERSION,"No da Versão do Leiaute do Arquivo"));
		arquivo.add(new Campo(167,171,"0","Densidade de Gravação do Arquivo"));
		
		campos.addAll(arquivo);
		
		campos.add(new Campo(172,191,"","Filler"));
		campos.add(new Campo(192,211,Constantes.REMESSA_PRODUCAO,"Situação do Arquivo"));
		campos.add(new Campo(212,215,"","Versão Aplicativo Caixa"));
		campos.add(new Campo(216,240,"","Filler"));
	}










	
	
}
