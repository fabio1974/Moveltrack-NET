package net.moveltrack.financeiro.action;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import com.lowagie.text.DocumentException;

import net.moveltrack.action.TableBean;
import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.domain.MBoletoTipo;
import net.moveltrack.domain.Permission;
import net.moveltrack.domain.TipoDeCobranca;





@Named
@SessionScoped
public class BoletoTable extends TableBean {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject MBoletoDao mBoletoDao;
	@Inject BoletoUtils boletoUtils;
	@Inject ClienteDao clienteDao;
	
	
	List<MBoleto> mBoletos;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "substring(nossoNumero,3)";
        if(loginBean.hasPermission(Permission.FATURA_VER_PROPRIA))
        	sortField = "dataVencimento";
        totalRows = mBoletoDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setFiltrosAcesso();
		refreshPage();
	}
	
	
	public List<MBoleto> getBoletos() {
        return mBoletos;
	}
	
	
	private void setFiltrosAcesso() {
		if(loginBean.hasPermission(Permission.FATURA_VER_PROPRIA)){
			try {
				cliente = clienteDao.findByUsuario(loginBean.getUsuario());
				Calendar c = Calendar.getInstance();  
				c.add(Calendar.DAY_OF_YEAR,32);
				dataVencimentoFim = c.getTime();  //Cliente só vê os boletos que vencerão daqui a 15 dias!
				
				Calendar c2 = Calendar.getInstance();  
				c2.add(Calendar.YEAR,-1);
				c2.add(Calendar.MONTH,-1);
				dataVencimentoInicio = c2.getTime();  //Cliente só vê os boletos de um ano para cá!
				
				situacaoNot1 = MBoletoStatus.CANCELADO;

			} catch (Exception e) {
				System.out.println("ERRO FILTRO!");
				e.printStackTrace();
			}
		}
	}
	
	
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		try {
			if(cliente!=null)
				sortField = "dataVencimento";
			setFilters();
			mBoletos = mBoletoDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = mBoletoDao.countAll(filters);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//throw new RuntimeException(e); // Handle it yourself.
		}
		lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage==0)
			currentPage=1;
	}


	private Date dataVencimentoFim;
	private Date dataVencimentoInicio;
	private String nossoNumero;
	private String numero;  //número da remessa
	private MBoletoStatus situacao;
	private MBoletoStatus situacaoNot1;
	private TipoDeCobranca tipoDeCobranca;
	private MBoletoTipo tipo;
	private Cliente cliente;
	private Empregado emissor;
	
	
	@Override
	public void setFilters() {
		preFilters.put("nossoNumero",nossoNumero);
		preFilters.put("situacao",situacao);
		preFilters.put("situacaoNot1",situacaoNot1);
		preFilters.put("tipoDeCobranca",tipoDeCobranca);
		preFilters.put("tipo",tipo);
		preFilters.put("contrato.clienteId",cliente);
		preFilters.put("emissorId",emissor);
		preFilters.put("remessa.numero",numero);
		preFilters.put("dataVencimentoFim",dataVencimentoFim);
		preFilters.put("dataVencimentoInicio",dataVencimentoInicio);
		normalizeFilters();
	}

	

	
	public void geraPdf(MBoleto mBoleto) throws IOException, DocumentException {
		
		if(mBoleto.getIugu()==null || mBoleto.getIugu().getCodigoBarrasImagem()==null)
			return;
		
		Date horaDoArquivo = mBoleto.getDataVencimento();
		
		SimpleDateFormat sdf = new SimpleDateFormat("-yyyyMMdd-HHmmss");
		
		String myFileName = "BoletoMoveltrack_"+mBoleto.getContrato().getCliente().getNome()+sdf.format(horaDoArquivo)+".pdf";
	    FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    ec.responseReset(); 
	    ec.setResponseContentType(ec.getMimeType(myFileName));
	    ec.setResponseHeader("Content-Disposition","attachment; filename=\"" + myFileName + "\""); 
	    
	    OutputStream output = ec.getResponseOutputStream();
	    
	    //output.write(boletoUtils.getBoletoInBytes(mBoleto,(ServletContext)ec.getContext()));
	    output.write(boletoUtils.getBoletoIuguInBytes(mBoleto,(ServletContext)ec.getContext()));

	    output.flush();
	    output.close();
	    fc.responseComplete(); 
	}	
	
	

	

	

	public MBoletoStatus[] getSituacoes(){
		return MBoletoStatus.values();
	}
	
	public MBoletoTipo[] getTipos(){
		return MBoletoTipo.values();
	}

	public TipoDeCobranca[] getTipoDeCobrancas(){
		return TipoDeCobranca.values();
	}
	

	public MBoletoStatus getSituacaoNot1() {
		return situacaoNot1;
	}


	public void setSituacaoNot1(MBoletoStatus situacaoNot1) {
		this.situacaoNot1 = situacaoNot1;
	}


	public Date getDataVencimentoInicio() {
		return dataVencimentoInicio;
	}


	public void setDataVencimentoInicio(Date dataVencimentoInicio) {
		this.dataVencimentoInicio = dataVencimentoInicio;
	}


	public Date getDataVencimentoFim() {
		return dataVencimentoFim;
	}


	public void setDataVencimentoFim(Date dataVencimentoFim) {
		this.dataVencimentoFim = dataVencimentoFim;
	}


	public Empregado getEmissor() {
		return emissor;
	}


	public void setEmissor(Empregado emissor) {
		this.emissor = emissor;
	}


	public TipoDeCobranca getTipoDeCobranca() {
		return tipoDeCobranca;
	}


	public void setTipoDeCobranca(TipoDeCobranca tipoDeCobranca) {
		this.tipoDeCobranca = tipoDeCobranca;
	}


	public MBoletoStatus getSituacao() {
		return situacao;
	}


	public void setSituacao(MBoletoStatus situacao) {
		this.situacao = situacao;
	}


	public MBoletoTipo getTipo() {
		return tipo;
	}


	public void setTipo(MBoletoTipo tipo) {
		this.tipo = tipo;
	}

	

	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public String getNossoNumero() {
		return nossoNumero;
	}


	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}


	public void setBoletos(List<MBoleto> mBoletos) {
		this.mBoletos = mBoletos;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	
	
}
