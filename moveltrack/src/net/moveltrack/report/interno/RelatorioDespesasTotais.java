package net.moveltrack.report.interno;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import net.moveltrack.dao.DespesaDao;
import net.moveltrack.domain.Despesa;
import net.moveltrack.domain.DespesaTipo;
import net.moveltrack.domain.PerfilTipo;
import net.moveltrack.util.Moeda;
import net.moveltrack.util.MyParagraph;

@Named
@RequestScoped
public class RelatorioDespesasTotais extends BaseAction implements Serializable{

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
				String myFileName = "DespesasTotaisMoveltrack.pdf";
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
		return "relatorioDespesasTotais";
	}
	
	
	public byte[] getReportInBytes() {
		Document document = new Document(PageSize.A4);
		try {

			PdfWriter.getInstance(document,os);
            document.open();
            
			PdfPTable header = UtilsReport.getCabecalho();
			header.setSpacingAfter(UtilsReport.interTables);
            document.add(header);
            
            String titulo = "RELATÓRIO DE DESPESAS - TOTALIZAÇÂO";
            List<String> linhas = new ArrayList<String>();
    		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm'h'");
            linhas.add("Emissão do Relatório: "+sdf1.format(new Date())+".");
            linhas.add("Inicio do Período: "+sdf1.format(inicio)+".");
            linhas.add("Fim   do  Período: "+sdf1.format(fim)+".");

			PdfPTable info = UtilsReport.getInformacoes(titulo,linhas);
			info.setSpacingAfter(2*UtilsReport.interTables);
            document.add(info);
            document.add(getTableDespesa());
            document.close();
            return os.toByteArray();

		} catch(Exception e){
        	e.printStackTrace();
        	return null;
        }
	}
	
	
	@Inject DespesaDao despesaDao; 
	
	private PdfPTable getTableDespesa() throws Exception {

		PdfPTable table = new PdfPTable(9);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Descrição",8,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Valor",1,Paragraph.ALIGN_CENTER));
        
		List<DespesaTipo> tipos = Arrays.asList(DespesaTipo.class.getEnumConstants());
		
		double totalDespesas = 0f;
		
		for (DespesaTipo tipo : tipos) {
			double totalPorTipo = despesaDao.getSomaDespesaPorTipoIntervalo(tipo,inicio,fim);
			totalDespesas+=totalPorTipo;
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,tipo.getDescricao(),8,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT, Moeda.mascaraDinheiro(totalPorTipo,Moeda.DINHEIRO_REAL_SEM_R$)+"    ", 1,Color.BLUE));
		}
        
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,"Total",8,Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalDespesas,Moeda.DINHEIRO_REAL_SEM_R$),1,Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(UtilsReport.interTables);
        
        return table;
	}


	public DespesaTipo[] getTipos() {
		return DespesaTipo.values();
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
