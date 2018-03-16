package net.moveltrack.report.interno;

import java.awt.Color;
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

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.LancamentoDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.Lancamento;
import net.moveltrack.domain.LancamentoStatus;
import net.moveltrack.domain.LancamentoTipo;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.util.Moeda;
import net.moveltrack.util.MyParagraph;

@Named
@RequestScoped
public class RelatorioReceitas extends BaseAction implements Serializable{

	private static final long serialVersionUID = 1L;
	private final ByteArrayOutputStream os = new ByteArrayOutputStream();

	private Date inicio;
	private Date fim;
	
	
	@PostConstruct
	public void init() {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH,1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		setInicio(c.getTime());
		
		c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND,59);
		c.set(Calendar.MILLISECOND,999);
		setFim(c.getTime());
		
		
	}
	
	public String geraPdf() {
		removeMessages();
		byte[] report = getReportInBytes();
		if(report!=null){
			try {
				String myFileName = "ReceitasMoveltrack.pdf";
				FacesContext fc = FacesContext.getCurrentInstance();
				ExternalContext ec = fc.getExternalContext();

				ec.responseReset(); 
				ec.setResponseContentType(ec.getMimeType(myFileName));
				ec.setResponseHeader("Content-Disposition","attachment; filename=\"" + myFileName + "\""); 

				OutputStream output = ec.getResponseOutputStream();
				output.write(report);

				output.flush();
				output.close();
				fc.responseComplete(); 
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return "relatorioReceitas";
	}
	
	
	public byte[] getReportInBytes() {
		Document document = new Document(PageSize.A4);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {

			PdfWriter.getInstance(document,os);
            document.open();
            
			PdfPTable header = UtilsReport.getCabecalho();
			header.setSpacingAfter(UtilsReport.interTables);
            document.add(header);
            
            String titulo = "RELATÓRIO DE RECEITAS - POR ORIGEM";
            List<String> linhas = new ArrayList<String>();
    		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm'h'");
            linhas.add("Emissão do Relatório: "+sdf1.format(new Date())+".");
            linhas.add("Inicio do Período: "+sdf1.format(inicio)+".");
            linhas.add("Fim   do  Período: "+sdf1.format(fim)+".");

			PdfPTable info = UtilsReport.getInformacoes(titulo,linhas);
			info.setSpacingAfter(2*UtilsReport.interTables);
            document.add(info);
    			
	        MyParagraph headerLast = new MyParagraph(Paragraph.ALIGN_LEFT,0,"Entradas Provinientes de Boletos Pagos",new Font(Font.TIMES_ROMAN, 10),6);
            document.add(headerLast);
            headerLast.setSpacingAfter(UtilsReport.interTables);
            document.add(getTableBoletos());
            
            headerLast = new MyParagraph(Paragraph.ALIGN_LEFT,0,"Entradas Provinientes de Pagamentos em Dinheiro/Cartão/Cheque",new Font(Font.TIMES_ROMAN, 10),6);
            document.add(headerLast);
	        headerLast.setSpacingAfter(2*UtilsReport.interTables);
	        document.add(getTableRecebimentos());
   		
            document.close();
            return os.toByteArray();
        } catch(Exception e){
        	e.printStackTrace();
        	return null;
        }
	}
	
	
	@Inject MBoletoDao mBoletoDao; 
	
	private PdfPTable getTableBoletos() throws Exception {
		List<MBoleto> boletos = mBoletoDao.findByStatusInicioFim(MBoletoStatus.PAGAMENTO_EFETUADO,inicio,fim);
		PdfPTable table = new PdfPTable(19);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Número",4,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Reg. do Pagamento",3,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Cliente",10,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Valor",2,Paragraph.ALIGN_CENTER));
        
        float total = 0f;
        for (MBoleto boleto: boletos) {
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        	total += boleto.getValor();
        	table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,boleto.getNossoNumero(),4,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,sdf.format(boleto.getDataRegistroPagamento()),3,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,boleto.getContrato().getCliente().getNome(),10,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT, Moeda.mascaraDinheiro(boleto.getValor(),Moeda.DINHEIRO_REAL_SEM_R$)+"    ",2,Color.BLUE));
		}
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,"Total",17,Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,Moeda.mascaraDinheiro(total,Moeda.DINHEIRO_REAL_SEM_R$),2,Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(2*UtilsReport.interTables);
        return table;
	}
	
	
	@Inject LancamentoDao lancamentoDao;
	
	private PdfPTable getTableRecebimentos() throws Exception {
		List<Lancamento> lancamentos= lancamentoDao.findByTipoInicioFim(LancamentoTipo.RECEBIMENTO_DE_CLIENTE,inicio,fim);
		PdfPTable table = new PdfPTable(19);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Dia Recebimento",3,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Cliente",9,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Forma de Pagamento",5,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Valor",2,Paragraph.ALIGN_CENTER));
        
        float total = 0f;
        for (Lancamento lancamento: lancamentos) {
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        	total += lancamento.getValor();
			table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,sdf.format(lancamento.getData()),3,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,lancamento.getOrdemDeServico()!=null?lancamento.getOrdemDeServico().getCliente().getNome():"",9,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,lancamento.getFormaPagamento().toString(),5,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT, Moeda.mascaraDinheiro(lancamento.getValor(),Moeda.DINHEIRO_REAL_SEM_R$)+"    ",2,Color.BLUE));
		}
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,"Total",17,Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,Moeda.mascaraDinheiro(total,Moeda.DINHEIRO_REAL_SEM_R$),2,Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(UtilsReport.interTables);
        return table;
	}
	



	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}
}
