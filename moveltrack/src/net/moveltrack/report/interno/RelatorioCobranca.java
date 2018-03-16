package net.moveltrack.report.interno;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.ContratoStatus;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.util.Moeda;
import net.moveltrack.util.MyParagraph;

@Named
@RequestScoped
public class RelatorioCobranca implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ByteArrayOutputStream os = new ByteArrayOutputStream();
	final float interTables = 10f;
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	Document document = new Document(PageSize.A3);
	PdfPTable table;
	float totalValor= 0f;
	float inadimplenciaTotal=0f;
	float totalParcelas =0f;
	
	
	@PostConstruct
	public void init() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR,-15);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		setFim(c.getTime());
	}	
	
	
	private Date fim;
	private MBoletoStatus status;
	private String sigla;
	
	
	
	
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public MBoletoStatus getStatus() {
		return status;
	}

	public void setStatus(MBoletoStatus status) {
		this.status = status;
	}
	
	public MBoletoStatus[] getStatuses() {
		return new MBoletoStatus[]{MBoletoStatus.VENCIDO,MBoletoStatus.PROTESTADO};
	}


	public void geraPdf() {
		try {
			String myFileName = "CobrancaMoveltrack.pdf";
		    FacesContext fc = FacesContext.getCurrentInstance();
		    ExternalContext ec = fc.getExternalContext();
	
		    ec.responseReset(); 
		    ec.setResponseContentType(ec.getMimeType(myFileName));
		    ec.setResponseHeader("Content-Disposition","attachment; filename=\"" + myFileName + "\""); 
		    
		    OutputStream output = ec.getResponseOutputStream();
		    output.write(getReportInBytes());
	
		    output.flush();
		    output.close();
		    fc.responseComplete(); 
		} catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	
	public byte[] getReportInBytes()  throws Exception{

			List<Contrato> contratos = getDevedores();
			PdfWriter.getInstance(document,os);
			document.open();
			
			PdfPTable header = UtilsReport.getCabecalho();
			header.setSpacingAfter(interTables);
            document.add(header);
			
            String titulo = "RELATÓRIO DE COBRANÇA";
            List<String> linhas = new ArrayList<String>();
    		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm'h'");

    		linhas.add("Devedores com pelo menos 01(uma) prestação vencida antes de "+sdf2.format(fim)+".");
            linhas.add("Emissão do Relatório: "+sdf1.format(new Date())+".");
    		linhas.add("Número de Devedores:"+contratos.size()+".");
            
			PdfPTable info = UtilsReport.getInformacoes(titulo,linhas);
			info.setSpacingAfter(interTables);
            document.add(info);
			
			for(Contrato contrato: contratos) {
				document.add(desenhaTabelaDeCobranca(contrato));
			}
			
			
			PdfPTable table = new PdfPTable(1);
			table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
			table.setWidthPercentage(100);
			PdfPCell c = new PdfPCell();
			c.addElement(new MyParagraph(Paragraph.ALIGN_CENTER,16,"VALOR TOTAL DE INADIMPLENCIA:    "+Moeda.mascaraDinheiro(inadimplenciaTotal,Moeda.DINHEIRO_REAL) ,new Font(Font.TIMES_ROMAN, 12),14));
			table.addCell(c);
			document.add(table);
			

			
			document.close();
			return os.toByteArray();
	}
	

	
	@SuppressWarnings("unchecked")
	public List<Contrato> getDevedores() {
		List<Contrato> list = new ArrayList<Contrato>();
		try{
			
			String filtroSigla = " ";
			if(sigla!=null)
				filtroSigla =  " and m.contrato.cliente.municipio.uf.sigla=:sigla "; 
			
			
			Query query = mBoletoDao.getEm().createQuery("select m.contrato from MBoleto m "
					+ " where " 
					+ " m.dataVencimento<:fim"
					+ " and m.situacao=:situacao "
					+ " and (m.contrato.status=:contratoStatus1 or m.contrato.status=:contratoStatus2)"
					+ filtroSigla
					+ " group by m.contrato"
					+ " order by m.contrato.cliente.nome");
	
			query.setParameter("fim",fim);
			query.setParameter("situacao",status);
			query.setParameter("contratoStatus1",ContratoStatus.ATIVO);
			query.setParameter("contratoStatus2",ContratoStatus.ENCERRADO);
			
			if(sigla!=null)
				query.setParameter("sigla",sigla);
			
			return (List<Contrato>)query.getResultList();		
		}catch(Exception e){
			e.printStackTrace();
			return list;
		}
	}
	
	

	@Inject MBoletoDao mBoletoDao;

	private PdfPTable desenhaTabelaDeCobranca(Contrato contrato) throws DocumentException {
		EntityManager em = mBoletoDao.getEm();
		Query query = em.createQuery("select m from MBoleto m "
				+ " where  "
				+ " m.situacao=:situacao and "

				+ " m.contrato.id=:contratoId "
				+ " order by m.dataVencimento");

		query.setParameter("situacao",status);
		query.setParameter("contratoId",contrato.getId());
		List<MBoleto> boletos = query.getResultList();

		table = new PdfPTable(16);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
		table.setWidthPercentage(100);
		
		Cliente cliente = contrato.getCliente();
		String nome = cliente.getNome();

		table.addCell(UtilsReport.getMyCellWithHeader("Cliente",nome,8));
		
		String contato = cliente.getTelefoneFixo()!=null?(cliente.getTelefoneFixo()+" / "):"";
		contato += cliente.getCelular1()!=null?(cliente.getCelular1()+" / "):"";
		contato += cliente.getCelular2()!=null?(cliente.getCelular2()+" / "):"";
		if(contato.endsWith(" / "))
			contato = contato.substring(0,contato.length()-2);
		table.addCell(UtilsReport.getMyCellWithHeader("Cidade",cliente.getMunicipio()!=null?cliente.getMunicipio().toString():"NI",4));
		table.addCell(UtilsReport.getMyCellWithHeader("Contato",contato,4));
		
		table.addCell(UtilsReport.getMyCellWithHeader("Endereço",cliente.getEndereco(),7));
		table.addCell(UtilsReport.getMyCellWithHeader("Número",cliente.getNumero(),2));
		table.addCell(UtilsReport.getMyCellWithHeader("Complemento",cliente.getComplemento()!=null?cliente.getComplemento():"",3));
		table.addCell(UtilsReport.getMyCellWithHeader("Bairro",cliente.getBairro(),4));

		table.addCell(UtilsReport.getHeader("Número Boleto",3,Paragraph.ALIGN_CENTER));
		table.addCell(UtilsReport.getHeader("Mensagem do Boleto",5,Paragraph.ALIGN_CENTER));
		table.addCell(UtilsReport.getHeader("Vencimento",3,Paragraph.ALIGN_CENTER));		
		table.addCell(UtilsReport.getHeader("Situação",3,Paragraph.ALIGN_CENTER));
		table.addCell(UtilsReport.getHeader("Valor",2,Paragraph.ALIGN_RIGHT));

		totalValor=0f;
		totalParcelas=0f;

		for (MBoleto mBoleto : boletos) {
			table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,mBoleto.getNossoNumero(),3,CMYKColor.BLACK));
			table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,mBoleto.getMensagem34(),5,CMYKColor.BLACK));
			table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,sdf2.format(mBoleto.getDataVencimento()),3,CMYKColor.BLACK));
			table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,mBoleto.getSituacao().toString(),3,CMYKColor.BLACK));
			table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,Moeda.mascaraDinheiro(mBoleto.getValor(),Moeda.DINHEIRO_REAL_SEM_R$),2,CMYKColor.BLACK));
			totalValor+=mBoleto.getValor();
			totalParcelas+=1;
		}
		table.addCell(UtilsReport.getHeader("Total   ",14,Paragraph.ALIGN_RIGHT));
		table.addCell(UtilsReport.getHeader(Moeda.mascaraDinheiro(totalValor,Moeda.DINHEIRO_REAL),2,Paragraph.ALIGN_RIGHT));
		
		inadimplenciaTotal += totalValor;
		
		table.setSpacingAfter(20f);
		return table;
	}
}
