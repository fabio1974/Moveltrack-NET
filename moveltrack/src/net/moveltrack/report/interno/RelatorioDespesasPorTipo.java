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
public class RelatorioDespesasPorTipo extends BaseAction implements Serializable{

	private static final long serialVersionUID = 1L;
	private final ByteArrayOutputStream os = new ByteArrayOutputStream();

	private DespesaTipo tipo;
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
				String myFileName = "DespesasMoveltrack.pdf";
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
		return "relatorioDespesasPorTipo";
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
            
            String titulo = "RELATÓRIO DE DESPESAS - POR TIPO";
            List<String> linhas = new ArrayList<String>();
    		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm'h'");
            linhas.add("Emissão do Relatório: "+sdf1.format(new Date())+".");
            linhas.add("Inicio do Período: "+sdf1.format(inicio)+".");
            linhas.add("Fim   do  Período: "+sdf1.format(fim)+".");

			PdfPTable info = UtilsReport.getInformacoes(titulo,linhas);
			info.setSpacingAfter(2*UtilsReport.interTables);
            document.add(info);
            
    		List<DespesaTipo> tipos = Arrays.asList(DespesaTipo.class.getEnumConstants());
    		
    		for (DespesaTipo tipoAux : tipos) {
    			
    			if((tipo!=null && tipo==tipoAux) ||tipo==null){
    			
	    			document.add(new MyParagraph(Paragraph.ALIGN_LEFT,0,"     ",new Font(Font.TIMES_ROMAN, 10,Font.BOLD),12));
	                document.add(new MyParagraph(Paragraph.ALIGN_LEFT,0,tipoAux.toString(),new Font(Font.TIMES_ROMAN, 10,Font.BOLD),12));
	                document.add(new MyParagraph(Paragraph.ALIGN_LEFT,0,"Data do Relatório: "+sdf.format(new Date()),new Font(Font.TIMES_ROMAN, 10),12));
	               	MyParagraph headerLast = new MyParagraph(Paragraph.ALIGN_LEFT,0,"Relatório do período de "+sdf.format(inicio)+ " até "+sdf.format(fim),new Font(Font.TIMES_ROMAN, 10),6);
	                document.add(headerLast);
	                headerLast.setSpacingAfter(UtilsReport.interTables);
	                document.add(getTableDespesaTipos(tipoAux));
                
    			}

    		}
    		
   		
            document.close();
            return os.toByteArray();
        } catch(Exception e){
        	e.printStackTrace();
        	return null;
        }
	}
	
	
	@Inject DespesaDao despesaDao; 
	
	private PdfPTable getTableDespesaTipos(DespesaTipo tipoAux) throws Exception {

		List<Despesa> despesas = despesaDao.getDespesaPorTipoIntervalo(tipoAux,inicio,fim);

		PdfPTable table = new PdfPTable(12);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("ID",1,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Data",2,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Descrição",8,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Valor",1,Paragraph.ALIGN_CENTER));
        
        float totalDespesas = 0f;
        for (Despesa despesa: despesas) {
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        	totalDespesas += despesa.getValor();
        	
        	table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,String.format("%05d",despesa.getId()),1,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_CENTER,sdf.format(despesa.getDataDaDespesa()),2,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,despesa.getDescricao(),8,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT, Moeda.mascaraDinheiro(despesa.getValor(),Moeda.DINHEIRO_REAL_SEM_R$)+"    ", 1,Color.BLUE));
		}
        
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,"Total",11,Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalDespesas,Moeda.DINHEIRO_REAL_SEM_R$),1,Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(UtilsReport.interTables);
        
        return table;
	}


	public DespesaTipo[] getTipos() {
		return DespesaTipo.values();
	}
	
	public DespesaTipo getTipo() {
		return tipo;
	}

	public void setTipo(DespesaTipo tipo) {
		this.tipo = tipo;
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
