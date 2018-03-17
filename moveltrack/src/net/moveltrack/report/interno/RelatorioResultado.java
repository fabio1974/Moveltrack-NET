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
import net.moveltrack.dao.LancamentoDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.DespesaTipo;
import net.moveltrack.domain.Lancamento;
import net.moveltrack.domain.LancamentoStatus;
import net.moveltrack.domain.LancamentoTipo;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.util.Moeda;
import net.moveltrack.util.MyParagraph;

@Named
@RequestScoped
public class RelatorioResultado extends BaseAction implements Serializable{

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
				String myFileName = "ResultadoMoveltrack.pdf";
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
            
            String titulo = "RESULTADO DO PERÍODO";
            List<String> linhas = new ArrayList<String>();
    		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm'h'");
            linhas.add("Emissão do Relatório: "+sdf1.format(new Date())+".");
            linhas.add("Inicio do Período: "+sdf1.format(inicio)+".");
            linhas.add("Fim   do  Período: "+sdf1.format(fim)+".");

			PdfPTable info = UtilsReport.getInformacoes(titulo,linhas);
			info.setSpacingAfter(2*UtilsReport.interTables);
            document.add(info);
            
            MyParagraph headerLast = new MyParagraph(Paragraph.ALIGN_LEFT,0,"RECEITAS",new Font(Font.TIMES_ROMAN, 10),6);
            document.add(headerLast);
            headerLast.setSpacingAfter(UtilsReport.interTables);
            document.add(getTableReceitas());
            
	        headerLast = new MyParagraph(Paragraph.ALIGN_LEFT,0,"DESPESAS",new Font(Font.TIMES_ROMAN, 10),6);
            document.add(headerLast);
            headerLast.setSpacingAfter(UtilsReport.interTables);
            document.add(getTableDespesa());
            
	        headerLast = new MyParagraph(Paragraph.ALIGN_LEFT,0,"RESULTADO FINAL",new Font(Font.TIMES_ROMAN, 10),6);
            document.add(headerLast);
            headerLast.setSpacingAfter(UtilsReport.interTables);
            document.add(getTableResultado());
   		
            document.close();
            return os.toByteArray();
        } catch(Exception e){
        	e.printStackTrace();
        	return null;
        }
	}
	
	
	double totalDespesas;
	double valorBoletos;
	double valorRecebimentos;
	
	@Inject DespesaDao despesaDao; 
	@Inject LancamentoDao lancamentoDao;
	@Inject MBoletoDao mBoletoDao;

	
	
	private PdfPTable getTableReceitas() throws Exception {
		valorBoletos = mBoletoDao.sumByStatusInicioFim(MBoletoStatus.PAGAMENTO_EFETUADO,inicio,fim);
		valorRecebimentos = lancamentoDao.sumByTipoInicioFim(LancamentoTipo.RECEBIMENTO_DE_CLIENTE,inicio,fim);

		PdfPTable table = new PdfPTable(9);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Descrição",8,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Valor",1,Paragraph.ALIGN_CENTER));
        
       	table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_LEFT,"Receitas oriundas de pagamento de boletos",8,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT, Moeda.mascaraDinheiro(valorBoletos,Moeda.DINHEIRO_REAL_SEM_R$)+"    ",1,Color.BLUE));
		
       	table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_LEFT,"Receitas de pagamentos em dinheiro/cheque/cartão",8,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT, Moeda.mascaraDinheiro(valorRecebimentos,Moeda.DINHEIRO_REAL_SEM_R$)+"    ",1,Color.BLUE));

        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,"TOTAL DAS RECEITAS NO PERÍODO",8,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,Moeda.mascaraDinheiro(valorBoletos+valorRecebimentos,Moeda.DINHEIRO_REAL_SEM_R$),1,Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(2*UtilsReport.interTables);
        return table;
	}
	
	
	private PdfPTable getTableDespesa() throws Exception {

		PdfPTable table = new PdfPTable(9);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Descrição",8,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Valor",1,Paragraph.ALIGN_CENTER));
        
		List<DespesaTipo> tipos = Arrays.asList(DespesaTipo.class.getEnumConstants());
		
		for (DespesaTipo tipo : tipos) {
			double totalPorTipo = despesaDao.getSomaDespesaPorTipoIntervalo(tipo,inicio,fim);
			totalDespesas+=totalPorTipo;
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,tipo.getDescricao(),8,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT, Moeda.mascaraDinheiro(totalPorTipo,Moeda.DINHEIRO_REAL_SEM_R$)+"    ", 1,Color.RED));
		}
        
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,"TOTAL DAS DESPESAS NO PERÍODO",8,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalDespesas,Moeda.DINHEIRO_REAL_SEM_R$),1,Color.RED,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(2*UtilsReport.interTables);

        return table;
	}
	

	
	
	
 
	
	private PdfPTable getTableResultado() throws Exception {
		PdfPTable table = new PdfPTable(9);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Descrição",8,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Valor",1,Paragraph.ALIGN_CENTER));
        
       	table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_LEFT,"Total de Receitas",8,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT, Moeda.mascaraDinheiro(valorBoletos+valorRecebimentos,Moeda.DINHEIRO_REAL_SEM_R$)+"    ",1,Color.BLUE));
        
       	table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_LEFT,"Total de Despesas",8,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT, Moeda.mascaraDinheiro(totalDespesas,Moeda.DINHEIRO_REAL_SEM_R$)+"    ",1,Color.RED));
		
		double resultado = valorBoletos + valorRecebimentos - totalDespesas;

        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,"RESULTADO DO PERÍODO",8,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.addCell(UtilsReport.getMyCell2(Paragraph.ALIGN_RIGHT,Moeda.mascaraDinheiro(resultado,Moeda.DINHEIRO_REAL_SEM_R$),1,resultado>0?Color.BLUE:Color.RED,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(2*UtilsReport.interTables);
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
