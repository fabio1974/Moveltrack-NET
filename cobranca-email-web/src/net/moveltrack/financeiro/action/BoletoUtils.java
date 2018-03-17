package net.moveltrack.financeiro.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.BoletoCarne3PorPagina;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.ParametrosBancariosMap;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeCobranca;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import net.moveltrack.dao.IuguDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.Carne;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.util.Moeda;

@Named
@Stateless
public class BoletoUtils implements Serializable {
	
	private static final long serialVersionUID = 8950967929492111534L;
	
	@Inject MBoletoDao mBoletoDao;
	
	public byte[] geraCarneCaixa(Carne carne,ServletContext servletContext) {
		List<Boleto> boletos = new ArrayList<Boleto>();
		List<MBoleto> mBoletos = mBoletoDao.findByCarne(carne);
    	for (MBoleto mBoleto : mBoletos) {
    		boletos.add(getBoletoFromMBoleto(mBoleto));
		}
	
    	String realPath = servletContext.getRealPath("/admin/pdf/BoletoCarne3PorPagina.pdf") ;
    	File templatePersonalizado = new File(realPath);
		byte[] boletosPorPagina = BoletoCarne3PorPagina.groupInPages(boletos,templatePersonalizado);    	
		return boletosPorPagina;		
	}
	
	
	public byte[] geraCarne(Carne carne,ServletContext servletContext) {
		List<MBoleto> mBoletos = mBoletoDao.findByCarne(carne);
		try {
			return getCarneIuguInBytes(mBoletos,servletContext);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public byte[] getBoletoInBytes(MBoleto mBoleto,ServletContext servletContext) {
		List<Boleto> boletos = new ArrayList<Boleto>();
		boletos.add(getBoletoFromMBoleto(mBoleto));

		String realPath = servletContext.getRealPath("/admin/pdf/BoletoTemplate.pdf");
		File templatePersonalizado = new File(realPath);
    	byte[] bytes = BoletoViewer.groupInOnePdfWithTemplate(boletos,templatePersonalizado);
		return bytes;
	}
	
	
	private final ByteArrayOutputStream os = new ByteArrayOutputStream();
	
	public byte[] getBoletoIuguInBytes(MBoleto mBoleto, ServletContext servletContext)  {
	
		try {

			Document document = new Document(PageSize.A4);

			PdfWriter.getInstance(document,os);
			document.open();

			LineSeparator ls = new LineSeparator();
			ls.setLineWidth(2);
			ls.setPercentage(120);

			document.newPage();
			document.add(new Chunk(ls));
			document.add(getBoletoTable(mBoleto,servletContext));
			document.add(new Chunk(ls));

			document.close();
			return os.toByteArray();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}

		
	}
	
	public byte[] getCarneIuguInBytes(List<MBoleto> mBoletos, ServletContext servletContext) throws DocumentException {

		Document document = new Document(PageSize.A4);
		

		PdfWriter.getInstance(document,os);
		document.open();
		
		LineSeparator ls = new LineSeparator();
		ls.setLineWidth(2);
		ls.setPercentage(120);
		
		int i = 0;
		
		while (i < mBoletos.size()) {
			
			document.newPage();
			document.add(new Chunk(ls));
			document.add(getBoletoTable(mBoletos.get(i++),servletContext));
			document.add(new Chunk(ls));
			
			if(i<mBoletos.size()){
				document.add(getBoletoTable(mBoletos.get(i++),servletContext));
				document.add(new Chunk(ls));
			}
			
			if(i<mBoletos.size()){
				document.add(getBoletoTable(mBoletos.get(i++),servletContext));
				document.add(new Chunk(ls));
			}	
		}

		document.close();
		return os.toByteArray();
	}
	
	@Inject IuguUtils iuguUtils;
	@Inject IuguDao iuguDao;

	@Transactional
	public PdfPTable getBoletoTable(MBoleto mBoleto, ServletContext servletContext){
		
	
		PdfPTable table = new PdfPTable(48);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
		table.setWidthPercentage(100);
		
		table.setSpacingBefore(3);
		
		table.addCell(UtilsReport.getImageCellFromPath(servletContext,"/admin/images/237.png",9));
		table.addCell(UtilsReport.getCellBoleto("BANCO BRADESCO S.A.",mBoleto.getIugu().getCodigoBarras(),39,Paragraph.ALIGN_LEFT));
		table.addCell(UtilsReport.getCellBoleto("LOCAL DE PAGAMENTO","Pagável em qualquer banco até o vencimento. Após vencimento, somente no Bradesco.",39,Paragraph.ALIGN_LEFT,8,9));
		table.addCell(UtilsReport.getCellBoleto("NOSSO NÚMERO",mBoleto.getNossoNumero(),9,Paragraph.ALIGN_RIGHT,8,9));
		table.addCell(UtilsReport.getCellBoleto("CEDENTE","Moveltrack Segurança & Tecnologia Ltda",28,Paragraph.ALIGN_LEFT,8,9));
		table.addCell(UtilsReport.getCellBoleto("CNPJ","17.547.013/0001-88",11,Paragraph.ALIGN_LEFT,8,9));		
		table.addCell(UtilsReport.getCellBoleto("VENCIMENTO",sdf.format(mBoleto.getDataVencimento()),9,Paragraph.ALIGN_RIGHT,8,9));
		
		Cliente cliente = mBoleto.getContrato().getCliente();
		
		table.addCell(UtilsReport.getCellBoleto("CLIENTE",cliente.getNome(),28,Paragraph.ALIGN_LEFT,8,9));
		table.addCell(UtilsReport.getCellBoleto("CPF/CNPJ",cliente.getCpf()!=null?cliente.getCpf():cliente.getCnpj(),11,Paragraph.ALIGN_LEFT,8,9));
		table.addCell(UtilsReport.getCellBoleto("VALOR DO DOC",Moeda.mascaraDinheiro(mBoleto.getValor(),Moeda.DINHEIRO_REAL),9,Paragraph.ALIGN_RIGHT,8,9));
		
		String mensagem34 = mBoleto.getMensagem34();
		table.addCell(UtilsReport.getCellBoleto("DESCRIÇÃO",mensagem34,39,Paragraph.ALIGN_LEFT,8,8));
		table.addCell(UtilsReport.getCellBoleto("MULTA/JUROS","",9,Paragraph.ALIGN_RIGHT,8,9));
		
		Calendar c = Calendar.getInstance();
		c.setTime(mBoleto.getDataVencimento());
		c.add(Calendar.DAY_OF_YEAR,30);
		
		table.addCell(UtilsReport.getCellBoleto("INSTRUÇÕES","Multa por atraso: 5%. Juros 1% ao mês. Não receber após "+ sdf.format(c.getTime()),39,Paragraph.ALIGN_LEFT,8,9));
		table.addCell(UtilsReport.getCellBoleto("VALOR A PAGAR",Moeda.mascaraDinheiro(mBoleto.getValor(),Moeda.DINHEIRO_REAL),9,Paragraph.ALIGN_RIGHT,8,9));
		
		table.addCell(UtilsReport.getImageCellFromByteArray(mBoleto.getIugu().getCodigoBarrasImagem(),32));
		table.addCell(UtilsReport.getImageCellFromPath(servletContext,"/assets/img/moveltrack/logo_azul_180_46.png",16,true,20));
		return table;

	}
	
	
	
	private Boleto getBoletoFromMBoleto(MBoleto mBoleto) {
		Boleto boleto = new Boleto(getTituloFromMyBoleto(mBoleto));

		boleto.setLocalPagamento("PREFERENCIALMENTE NAS CASAS LOTÉRICAS ATÉ O VALOR LIMITE");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		boleto.setInstrucao1(mBoleto.getMensagem34()==null?"":mBoleto.getMensagem34().endsWith(".")?mBoleto.getMensagem34():(mBoleto.getMensagem34()+"."));
		
		try {
			String inst1 = boleto.getInstrucao1();
			inst1 = inst1.substring(0,1).toUpperCase() + inst1.substring(1);
			boleto.setInstrucao1(inst1);
		} catch (Exception e) {}
		
		
		boleto.setInstrucao5("Pagamentos após o vencimento: multa de "+mBoleto.getMulta()+"% + juros de "+mBoleto.getJuros()+"% ao mês.");
		boleto.setInstrucao6("O atraso de 7 dias no pagamento suspenderá temporariamente o rastreamento.");
		boleto.setInstrucao7("Número do Contrato:"+mBoleto.getContrato().getNumeroContrato()+". Início do Contrato: "+sdf.format(mBoleto.getContrato().getInicio()));
		
		boleto.addTextosExtras("txtRsAgenciaCodigoCedente", "3418/384460-9");
		boleto.addTextosExtras("txtFcAgenciaCodigoCedente", "3418/384460-9");		

		return boleto;
	}

	
	private Titulo getTituloFromMyBoleto(MBoleto mBoleto) {
		Cedente cedente = new Cedente("MOVELTRACK SEGURANCA & TECNOLOGIA","17.547.013/0001-88");
		Cliente cliente = mBoleto.getContrato().getCliente();
		
		Sacado sacado = new Sacado(cliente.getNome(),cliente.getCpf()==null?cliente.getCnpj():cliente.getCpf());
		sacado.addEndereco(getEndereco(mBoleto.getContrato().getCliente()));

		Titulo titulo = new Titulo(getContaBancaria(TipoDeCobranca.valueOf(mBoleto.getTipoDeCobranca().toString())), sacado, cedente);
		titulo.setDataDoDocumento(mBoleto.getDataEmissao());
		titulo.setNumeroDoDocumento(mBoleto.getNossoNumero().substring(6));
		titulo.setNossoNumero(mBoleto.getNossoNumero().substring(2));
		titulo.setDigitoDoNossoNumero(getNossoNumeroDV(mBoleto.getNossoNumero()));
		titulo.setDataDoVencimento(mBoleto.getDataVencimento());
		titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
		titulo.setParametrosBancarios(new ParametrosBancariosMap("CodigoOperacao", new Integer(870)));
		titulo.setValor(new BigDecimal(mBoleto.getValor()));
		
		return titulo;
	}
	
	
    private Endereco getEndereco(Cliente cliente){
		Endereco enderecoSac = new Endereco();
		try{
			enderecoSac.setUF(UnidadeFederativa.valueOfSigla(cliente.getMunicipio().getUf().getSigla()));
			enderecoSac.setLocalidade(cliente.getMunicipio().getDescricao());
		}catch(Exception e){}	
		
		enderecoSac.setCep(new CEP(cliente.getCep()));
		enderecoSac.setBairro(cliente.getBairro() == null ? "" : cliente.getBairro());
		enderecoSac.setLogradouro(cliente.getEndereco());
		enderecoSac.setNumero(cliente.getNumero());
		enderecoSac.setComplemento(cliente.getComplemento());
		return enderecoSac;
    }
	
	private ContaBancaria getContaBancaria(TipoDeCobranca tipoDeCobranca) {
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.CAIXA_ECONOMICA_FEDERAL.create());
		contaBancaria.setNumeroDaConta(new NumeroDaConta(384460, "9"));
		contaBancaria.setCarteira(new Carteira(2,tipoDeCobranca));
		contaBancaria.setAgencia(new Agencia(3418));
		return contaBancaria;
	}
	
	private  String getNossoNumeroDV(String nossoNumero){
		int m = 1;
		int dv;
		int soma = 0;
		String nn = nossoNumero;
		for (int i = nn.length()-1; i>=0 ; i--) {
			char c = nn.charAt(i);
			int n = Character.getNumericValue(c);
			m+=1;
			if(m==10)
				m = 2;
			soma += m*n;
		}
		if(soma < 11)
			dv = 11 - soma;
		else
			dv = 11 - (soma%11);
		if(dv>9)
			dv = 0;
		return String.valueOf(dv) ;
	}



}
