package net.moveltrack.report.interno;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.ContratoStatus;
import net.moveltrack.domain.EquipamentoSituacao;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.Operadora;
import net.moveltrack.domain.RelatorioSinalRegistro;
import net.moveltrack.domain.TempoTipo;
import net.moveltrack.domain.VeiculoStatus;
import net.moveltrack.domain.VeiculoTipo;
import net.moveltrack.util.MyParagraph;
import net.moveltrack.util.Utils;


@Named
@SessionScoped
public class RelatorioSinal extends BaseAction implements Serializable{

	private final ByteArrayOutputStream os = new ByteArrayOutputStream();
	int qtde;
	
	
	private String imei;
	private String numero;
	private Operadora operadora;
    private EquipamentoSituacao equipamentoSituacao;
    private ModeloRastreador modelo;
    private ContratoStatus contratoStatus;
    private String placa;
    private Integer inicio;
    private Integer fim;
    
    
    
    @Enumerated(EnumType.STRING)
    private TempoTipo inicioTempoTipo;

    @Enumerated(EnumType.STRING)
    private TempoTipo fimTempoTipo;
    
    @Enumerated(EnumType.STRING)
    private VeiculoTipo tipo;
    
    @Enumerated(EnumType.STRING)
    private VeiculoStatus veiculoStatus;


    private String nome;
	
	@PostConstruct
	public void init() {
		setContratoStatus(ContratoStatus.ATIVO);
		setVeiculoStatus(VeiculoStatus.ATIVO);
		setEquipamentoSituacao(EquipamentoSituacao.ATIVO);
		setInicio(0);
		setInicioTempoTipo(TempoTipo.MINUTOS);
	}
	
	
	public String geraPdf() {
		removeMessages();
		byte[] report = getReportInBytes();
		if(report!=null){
			try {
				String myFileName = "SinalMoveltrack.pdf";
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
		return "relatorioSinal";
	}
	
	public byte[] getReportInBytes() {
		try {
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
            linhas.add("Operadora:"+ (operadora!=null?operadora.toString():"----"));//+" Ordenação: "+ (order!=null?order.toString():"POR_NOME_PROPRIETARIO"));
            
			PdfPTable info = UtilsReport.getInformacoes(titulo,linhas);
			info.setSpacingAfter(UtilsReport.interTables);
            document.add(info);
            
            document.add(buildTable());
            document.add(new MyParagraph(Paragraph.ALIGN_LEFT,0,"Quantidade: "+qtde,new Font(Font.TIMES_ROMAN, 10, Font.BOLD),12));
            document.close();
            return os.toByteArray();
		} catch (Exception e) {
			return null;
		}
            
	}
	

	private PdfPTable buildTable() throws Exception{
		List<RelatorioSinalRegistro> list = getList();
		qtde = list.size();

		PdfPTable table = new PdfPTable(55);
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setWidthPercentage(100);
      
        table.addCell(UtilsReport.getHeader("Imei",6,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Senha",3,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Modelo",4,Paragraph.ALIGN_CENTER));        
        table.addCell(UtilsReport.getHeader("Numero",5,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("OP",2,Paragraph.ALIGN_CENTER));        
        table.addCell(UtilsReport.getHeader("Eq. Status",4,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Leit. GPS",4,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Contr. Status",4,Paragraph.ALIGN_CENTER));        
        table.addCell(UtilsReport.getHeader("Placa",4,Paragraph.ALIGN_CENTER));
        table.addCell(UtilsReport.getHeader("Modelo",6,Paragraph.ALIGN_LEFT));
        table.addCell(UtilsReport.getHeader("Tipo",4,Paragraph.ALIGN_LEFT));
        table.addCell(UtilsReport.getHeader("Nome",10,Paragraph.ALIGN_CENTER));
        
        for(RelatorioSinalRegistro re: list){
        	Color bgColor = isInadimplente(re.getImei())?Color.LIGHT_GRAY:Color.WHITE;
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getImei(),6,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getSenha(),3,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getModelo()!=null?re.getModelo().toString():"",4,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getNumero(),5,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,UtilsReport.getOperadoraShort(re.getOperadora()),2,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getEquipamentoSituacao()!=null?re.getEquipamentoSituacao().toString():"",4,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,Utils.getTimeSinceDate(re.getDateLocation()),4,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getContratoStatus()!=null?re.getContratoStatus().toString():"",4,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getPlaca(),4,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getMarcaModelo(),6,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getTipo()!=null?re.getTipo().toString():"",4,Color.BLACK,bgColor));
			table.addCell(UtilsReport.getMyCell2(PdfPCell.ALIGN_CENTER,re.getNome(),10,Color.BLACK,bgColor));
        }
        table.setSpacingAfter(2*UtilsReport.interTables);
        return table;
	}
	
	@Inject EquipamentoDao equipamentoDao;
	
	private List<RelatorioSinalRegistro> getList() throws Exception{
		String where = " where 1 ";
		
		if(inicio!=null && inicio >0){
			
			if (inicioTempoTipo == null) {
				operacaoErro("error.relatorioInterno.atrasoMinimo.nao.informado","fimTempoTipo",false);
				throw new Exception("Erro de Relatório");
			}	

			
			String unidade=null;
			switch (inicioTempoTipo) {
			case DIAS: unidade = "DAY"; break;
			case HORAS: unidade = "HOUR"; break;
			case MINUTOS: unidade = "MINUTE";break;
			}
			where += " and (TIMESTAMPDIFF("+unidade+",aux.dateLocation,now()) >=:inicio or aux.dateLocation is null)";
		}
		
		
		if(fim!=null && fim>0){
			
			if (fimTempoTipo == null) {
				operacaoErro("error.relatorioInterno.atrasoMaximo.nao.informado","fimTempoTipo",false);
				throw new Exception("Erro de Relatório");
			}	
			
			String unidade=null;
			switch (fimTempoTipo) {
			case DIAS: unidade = "DAY"; break;
			case HORAS: unidade = "HOUR"; break;
			case MINUTOS: unidade = "MINUTE";break;
			}
			where += " and TIMESTAMPDIFF("+unidade+",aux.dateLocation,now()) <=:fim";
		}
		
		
		
		if(StringUtils.isNotEmpty(imei))
			where += " and e.imei =:imei";
		if(StringUtils.isNotEmpty(numero))
			where += " and ch.numero =:numero";
		if(StringUtils.isNotEmpty(nome))
			where += " and p.nome =:nome";
		if(operadora!=null)
			where += " and ch.operadora=:operadora";
		if(modelo!=null)
			where += " and e.modelo=:modelo";		
		if(equipamentoSituacao!=null)
			where += " and e.situacao=:equipamentoSituacao";
		if(contratoStatus!=null)
			where += " and c.status =:contratoStatus";
		if(StringUtils.isNotEmpty(placa))
			where += " and v.placa like :placa";
		if(tipo!=null)
			where += " and v.tipo =:tipo";
		if(veiculoStatus!=null)
			where += " and v.status =:status";
		
		
		String sql = " select e.imei,e.senha,e.modelo,ch.numero, ch.operadora, e.situacao as equipamentoSituacao,  aux.dateLocation,"
				+" c.status as contratoStatus,v.tipo, v.placa,v.marcaModelo,p.nome "
				+" from equipamento e"
				+" left join (select l.imei, max(l.dateLocation) as dateLocation from location l group by l.imei) as aux" 
				+" on aux.imei = e.imei"

				+" left join veiculo v on v.equipamento_id = e.id"
				+" left join contrato c on c.id = v.contrato_id"
				+" left join pessoa p on p.id = c.cliente_id"
				+" left join chip ch on ch.id = e.chip_id"

				+ where

				+" order by aux.dateLocation ";

		Query query = equipamentoDao.getEm().createNativeQuery(sql,RelatorioSinalRegistro.class);
		if(StringUtils.isNotEmpty(imei))
			query.setParameter("imei",imei);
		if(StringUtils.isNotEmpty(numero))
			query.setParameter("numero",numero);
		if(StringUtils.isNotEmpty(nome))
			query.setParameter("nome",nome);
		if(operadora!=null)
			query.setParameter("operadora",operadora.toString());
		if(modelo!=null)
			query.setParameter("modelo",modelo.toString());		
		if(equipamentoSituacao!=null)
			query.setParameter("equipamentoSituacao",equipamentoSituacao.toString());
		if(contratoStatus!=null)
			query.setParameter("contratoStatus",contratoStatus.toString());
		if(StringUtils.isNotEmpty(placa))
			query.setParameter("placa","%"+placa+"%");
		if(tipo!=null)
			query.setParameter("tipo",tipo.toString());
		
		if(veiculoStatus!=null)
			query.setParameter("status",veiculoStatus.toString());
		
		if(inicio!=null && inicio>0)
			query.setParameter("inicio",inicio);
		
		if(fim!=null && fim>0)
			query.setParameter("fim",fim);
		
		
		return (List<RelatorioSinalRegistro>)query.getResultList();
	}


	@Inject ContratoDao contratoDao;
	@Inject VeiculoDao veiculoDao;
	@Inject MBoletoDao mBoletoDao;

	private boolean isInadimplente(String imei) {
		try {
			Contrato contrato = veiculoDao.findByEquipamento(imei).getContrato();
			return mBoletoDao.findByClienteStatus(contrato.getCliente(),MBoletoStatus.VENCIDO).size() > 0  || 
				   mBoletoDao.findByClienteStatus(contrato.getCliente(),MBoletoStatus.PROTESTADO).size()>0 ;
		} catch (Exception e) {
			return false;
		}
	}
	
	public VeiculoTipo[] getTipos(){
		return VeiculoTipo.values();
	}
	
	public VeiculoStatus[] getVeiculoStatuses(){
		return VeiculoStatus.values();
	}
	
	public ContratoStatus[] getContratoStatuses(){
		return ContratoStatus.values();
	}
	
	public ModeloRastreador[] getModelos(){
		return ModeloRastreador.values();
	}
	
	public EquipamentoSituacao[] getEquipamentoSituacaos(){
		return EquipamentoSituacao.values();
	}
	
	public Operadora[] getOperadoras(){
		return Operadora.values();
	}
	
	public TempoTipo[] getTempoTipos(){
		return TempoTipo.values();
	}

	


	public TempoTipo getInicioTempoTipo() {
		return inicioTempoTipo;
	}


	public void setInicioTempoTipo(TempoTipo inicioTempoTipo) {
		this.inicioTempoTipo = inicioTempoTipo;
	}


	public TempoTipo getFimTempoTipo() {
		return fimTempoTipo;
	}


	public void setFimTempoTipo(TempoTipo fimTempoTipo) {
		this.fimTempoTipo = fimTempoTipo;
	}


	public Integer getInicio() {
		return inicio;
	}

	public void setInicio(Integer inicio) {
		this.inicio = inicio;
	}

	public Integer getFim() {
		return fim;
	}

	public void setFim(Integer fim) {
		this.fim = fim;
	}


	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Operadora getOperadora() {
		return operadora;
	}

	public void setOperadora(Operadora operadora) {
		this.operadora = operadora;
	}

	public EquipamentoSituacao getEquipamentoSituacao() {
		return equipamentoSituacao;
	}

	public void setEquipamentoSituacao(EquipamentoSituacao equipamentoSituacao) {
		this.equipamentoSituacao = equipamentoSituacao;
	}

	public ContratoStatus getContratoStatus() {
		return contratoStatus;
	}

	public void setContratoStatus(ContratoStatus contratoStatus) {
		this.contratoStatus = contratoStatus;
	}
	
	public VeiculoStatus getVeiculoStatus() {
		return veiculoStatus;
	}

	public void setVeiculoStatus(VeiculoStatus veiculoStatus) {
		this.veiculoStatus = veiculoStatus;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public VeiculoTipo getTipo() {
		return tipo;
	}

	public void setTipo(VeiculoTipo tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public ModeloRastreador getModelo() {
		return modelo;
	}


	public void setModelo(ModeloRastreador modelo) {
		this.modelo = modelo;
	}	
	
	
	
}	