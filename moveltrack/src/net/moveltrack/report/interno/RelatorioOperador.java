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
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.EmpregadoDao;
import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.dao.LancamentoDao;
import net.moveltrack.dao.OrdemDeServicoDao;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.Lancamento;
import net.moveltrack.domain.LancamentoTipo;
import net.moveltrack.domain.OrdemDeServico;
import net.moveltrack.domain.PerfilTipo;
import net.moveltrack.domain.TempoTipo;
import net.moveltrack.domain.VeiculoStatus;
import net.moveltrack.domain.VeiculoTipo;
import net.moveltrack.util.Moeda;


@Named
@SessionScoped
public class RelatorioOperador extends BaseAction implements Serializable{

	private static final long serialVersionUID = 1L;
	private final ByteArrayOutputStream os = new ByteArrayOutputStream();

	int qtde;
	private Empregado operador;
    private Date inicio;
    private Date fim;
    private List<Empregado> instaladores;
    
    @Inject EmpregadoDao empregadoDao;
	@Inject EquipamentoDao equipamentoDao;
	@Inject OrdemDeServicoDao ordemDeServicoDao;
	@Inject LancamentoDao lancamentoDao;

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
		
		instaladores = empregadoDao.findByTipo(PerfilTipo.INSTALADOR);
	}
	
	
	public String geraPdf() {
		removeMessages();
		byte[] report = getReportInBytes();
		if(report!=null){
			try {
				String myFileName = "OperadorMoveltrack.pdf";
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
		return "relatorioOperador";
	}
	
	public byte[] getReportInBytes() {
		try {
			
			Calendar c = Calendar.getInstance();
			c.setTime(fim);
			c.set(Calendar.HOUR_OF_DAY,23);
			c.set(Calendar.MINUTE,59);
			c.set(Calendar.SECOND,59);
			c.set(Calendar.MILLISECOND,999);
			setFim(c.getTime());
			
			Document document = new Document(PageSize.A3);
            PdfWriter.getInstance(document,os);
            document.open();
            
			PdfPTable header = UtilsReport.getCabecalho();
			header.setSpacingAfter(UtilsReport.interTables);
            document.add(header);
			
            String titulo = "RELATÓRIO DE SINAL";
            List<String> linhas = new ArrayList<String>();
    		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm'h'");
            linhas.add("Emissão do Relatório: "+sdf1.format(new Date())+".");
            linhas.add("Instalador: "+operador.getNome());
            linhas.add("Inicio do Período: "+sdf1.format(inicio)+".");
            linhas.add("Fim   do  Período: "+sdf1.format(fim)+".");


			PdfPTable info = UtilsReport.getInformacoes(titulo,linhas);
			info.setSpacingAfter(2*UtilsReport.interTables);
            document.add(info);
            
            PdfPTable oss = buildTableOS();
            oss.setSpacingAfter(2*UtilsReport.interTables);
            document.add(oss);
            
            PdfPTable recebimentos = buildTableRecebimento();
            recebimentos.setSpacingAfter(2*UtilsReport.interTables);
            document.add(recebimentos);

            PdfPTable devolucao =buildTableDevolucao();
            devolucao.setSpacingAfter(2*UtilsReport.interTables);
            document.add(devolucao);
            
            PdfPTable vales= buildTableVale();
            vales.setSpacingAfter(2*UtilsReport.interTables);
            document.add(vales);
            
            PdfPTable gastos = buildTableGastos();
            gastos.setSpacingAfter(2*UtilsReport.interTables);
            document.add(gastos);
            
            PdfPTable somatorio = buildTableSomatorio();
            somatorio.setSpacingAfter(UtilsReport.interTables);
            document.add(somatorio);
            
            PdfPTable pagamento = buildTablePagamento();
            pagamento.setSpacingAfter(UtilsReport.interTables);
            document.add(pagamento);

            
            
            
            document.close();
            return os.toByteArray();
		} catch (Exception e) {
			return null;
		}
            
	}
	
	 double totalServicos = 0d;
	 double totalRecebimentos = 0d;
	 double totalDevolucao = 0d;
	 double totalValesCadastrados = 0d;
	 double totalGastos = 0d;
	 double total = 0d;

	private PdfPTable buildTableOS() throws Exception{
		List<OrdemDeServico> list = ordemDeServicoDao.findByInicioFimOperador(inicio,fim,operador);
		qtde = list.size();

		PdfPTable table = new PdfPTable(52);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
      
        table.addCell(UtilsReport.getHeader("OS",3,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Data Servico",6,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Serviço",6,Paragraph.ALIGN_CENTER));        
        table.addCell(UtilsReport.getHeader("Operador",15,Paragraph.ALIGN_LEFT));
        table.addCell(UtilsReport.getHeader("Observacao",18,Paragraph.ALIGN_LEFT));        
        table.addCell(UtilsReport.getHeader("Valor",4,Paragraph.ALIGN_RIGHT));
        
        totalServicos = 0d;
        
        for(OrdemDeServico os: list){
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,os.getNumero(),3,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,sdf.format(os.getDataDoServico()),6,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,os.getServico().toString(),6,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getCliente().getNome()+"/"+os.getVeiculo().getMarcaModelo(),15,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getObservacao(),18,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(os.getValorDoServico(),Moeda.DINHEIRO_REAL),4,Color.BLACK));
			totalServicos += os.getValorDoServico();
        }
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"TOTAL DE SERVIÇOS",48,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalServicos,Moeda.DINHEIRO_REAL),4,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(UtilsReport.interTables);
        return table;
	}
	
	
	
	
	private PdfPTable buildTableRecebimento() {
		List<Lancamento> list = lancamentoDao.findByInicioFimOperadorTipo(inicio,fim,operador,LancamentoTipo.RECEBIMENTO_DE_CLIENTE); 
		qtde = list.size();

		PdfPTable table = new PdfPTable(52);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Data Recebimento",6,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Forma de Recebimento",5,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Cliente",10,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Entregue a",10,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Observacao",17,Paragraph.ALIGN_CENTER));        
        table.addCell(UtilsReport.getHeader("Valor",4,Paragraph.ALIGN_CENTER));
        
        totalRecebimentos = 0d;
        
        for(Lancamento os: list){
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,sdf.format(os.getData()),6,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,os.getFormaPagamento().toString(),5,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getOrdemDeServico().getCliente().getNome(),10,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getOperador().getNome(),10,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getObservacao(),17,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(os.getValor(),Moeda.DINHEIRO_REAL),4,Color.BLACK));
			totalRecebimentos += os.getValor();
        }
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"TOTAL DE RECEBIMENTOS",48,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalRecebimentos,Moeda.DINHEIRO_REAL),4,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(UtilsReport.interTables);
        return table;
	}
	
	

	


	private PdfPTable buildTableDevolucao() {
		List<Lancamento> list = lancamentoDao.findByInicioFimOperadorTipo(inicio,fim,operador,LancamentoTipo.DEVOLUCAO_DE_DINHEIRO); 
		qtde = list.size();

		PdfPTable table = new PdfPTable(52);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Data da Dev.",6,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Entregue a",21,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Observacao",21,Paragraph.ALIGN_CENTER));        
        table.addCell(UtilsReport.getHeader("Valor",4,Paragraph.ALIGN_CENTER));
        
        totalDevolucao = 0d;
        
        for(Lancamento os: list){
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,sdf.format(os.getData()),6,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getOperador().getNome(),21,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getObservacao(),21,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(os.getValor(),Moeda.DINHEIRO_REAL),4,Color.BLACK));
			totalDevolucao += os.getValor();
        }
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"TOTAL DE DEVOLUÇÃO",48,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalDevolucao,Moeda.DINHEIRO_REAL),4,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(UtilsReport.interTables);
        return table;
	}
	
	
	private PdfPTable buildTableVale() {
		List<Lancamento> list = lancamentoDao.findByInicioFimOperadorTipo(inicio,fim,operador,LancamentoTipo.VALE); 
		qtde = list.size();

		PdfPTable table = new PdfPTable(52);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Data do Vale",6,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Feito por",21,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Observacao",21,Paragraph.ALIGN_CENTER));        
        table.addCell(UtilsReport.getHeader("Valor",4,Paragraph.ALIGN_CENTER));
        
        totalValesCadastrados = 0d;
        
        for(Lancamento os: list){
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,sdf.format(os.getData()),6,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getOperador().getNome(),21,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getObservacao(),21,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(os.getValor(),Moeda.DINHEIRO_REAL),4,Color.BLACK));
			totalValesCadastrados += os.getValor();
        }
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"TOTAL DE VALES",48,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalValesCadastrados,Moeda.DINHEIRO_REAL),4,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(UtilsReport.interTables);
        return table;
	}
	
	
	private PdfPTable buildTableGastos() {
		List<Lancamento> list = lancamentoDao.findByInicioFimOperadorTipo(inicio,fim,operador,LancamentoTipo.GASTO_DE_MATERIAL); 
		qtde = list.size();

		PdfPTable table = new PdfPTable(52);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("Data do Gasto",6,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Registrado por",21,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Observacao",21,Paragraph.ALIGN_CENTER));        
        table.addCell(UtilsReport.getHeader("Valor",4,Paragraph.ALIGN_CENTER));
        
        totalGastos = 0d;
        
        for(Lancamento os: list){
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,sdf.format(os.getData()),6,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getOperador().getNome(),21,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_LEFT,os.getObservacao(),21,Color.BLACK));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(os.getValor(),Moeda.DINHEIRO_REAL),4,Color.BLACK));
			totalGastos += os.getValor();
        }
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"TOTAL DE GASTOS",48,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalGastos,Moeda.DINHEIRO_REAL),4,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
        table.setSpacingAfter(UtilsReport.interTables);
        return table;
	}
	
	
	
	private PdfPTable buildTableSomatorio() {
		PdfPTable table = new PdfPTable(52);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("DINHEIRO COM OPERADOR",48,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Valor",4,Paragraph.ALIGN_CENTER));
        
/* 		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"SERVICOS",48,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalServicos,Moeda.DINHEIRO_REAL),4,Color.BLUE));
*/
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"RECEBIMENTOS",48,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalRecebimentos,Moeda.DINHEIRO_REAL),4,Color.BLUE));

		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"DEVOLUÇÕES",48,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(-1*totalDevolucao,Moeda.DINHEIRO_REAL),4,Color.RED));
		
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"GASTOS COM MATERIAL",48,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(-1*totalGastos,Moeda.DINHEIRO_REAL),4,Color.RED));
		
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"VALES CADASTRADOS",48,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(-1*totalValesCadastrados,Moeda.DINHEIRO_REAL),4,Color.RED));
       
		total = totalRecebimentos - totalDevolucao - totalValesCadastrados - totalGastos;
		
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"TOTAL NA MÃO DO OPERADOR",48,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(total,Moeda.DINHEIRO_REAL),4,total<0?Color.RED:Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
		
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"VALE DE CORREÇÃO",48,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(-1*total,Moeda.DINHEIRO_REAL),4,Color.RED));
		
		valeTotal = total+totalValesCadastrados;
		
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"VALE TOTAL",48,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(-1*valeTotal,Moeda.DINHEIRO_REAL),4,Color.RED,UtilsReport.FAIRLY_LIGTH_GRAY));

        table.setSpacingAfter(2*UtilsReport.interTables);
        return table;
	}

	double valeTotal = 0d;

	private PdfPTable buildTablePagamento() {
		PdfPTable table = new PdfPTable(52);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
        
        table.addCell(UtilsReport.getHeader("PARCELAS PARA PAGAMENTO DO OPERADOR",48,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Valor",4,Paragraph.ALIGN_CENTER));
        
 		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"SERVICOS",48,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(totalServicos,Moeda.DINHEIRO_REAL),4,Color.BLUE));
		
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"SALARIO",48,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(operador.getSalario(),Moeda.DINHEIRO_REAL),4,Color.BLUE));

		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"VALE TOTAL",48,Color.BLACK));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(-1*valeTotal,Moeda.DINHEIRO_REAL),4,Color.RED));

		total = totalServicos + operador.getSalario() - valeTotal;
		
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,"TOTAL A PAGAR",48,Color.BLACK,UtilsReport.FAIRLY_LIGTH_GRAY));
		table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_RIGHT,Moeda.mascaraDinheiro(total,Moeda.DINHEIRO_REAL),4,total<0?Color.RED:Color.BLUE,UtilsReport.FAIRLY_LIGTH_GRAY));
		
        table.setSpacingAfter(UtilsReport.interTables);
        return table;
	}
	

	
		




	public Empregado getOperador() {
		return operador;
	}


	public void setOperador(Empregado operador) {
		this.operador = operador;
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


	public List<Empregado> getInstaladores() {
		return instaladores;
	}


	public void setInstaladores(List<Empregado> instaladores) {
		this.instaladores = instaladores;
	}



	
	
}	