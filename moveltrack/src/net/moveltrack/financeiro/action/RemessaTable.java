package net.moveltrack.financeiro.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import net.moveltrack.action.TableBean;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.dao.RemessaDao;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.Remessa;
import net.moveltrack.financeiro.cnabcaixa.model.ArquivoRemessa;




@Named
@SessionScoped
public class RemessaTable extends TableBean {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	RemessaDao remessaDao;
	
	@Inject 
	MBoletoDao mBoletoDao;
	
	boolean fechamentoReadOnly;
	
	List<Remessa> remessas;
	List<MBoleto> mBoletos ;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = remessaDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setFiltrosAcesso();
		refreshPage();
	}
	
	
	
	public void geraArquivo(String numero) throws IOException {
		Date horaDoArquivo = new Date();
		String myFileName = "E1"+StringUtils.leftPad(numero,6,"0")+".REM";
	    FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    ec.responseReset(); 
	    ec.setResponseContentType(ec.getMimeType(myFileName));
	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + myFileName + "\""); 
	    
	    OutputStream output = ec.getResponseOutputStream();
	    PrintWriter writer = new PrintWriter(output);
	    
	    mBoletos = mBoletoDao.findNaoCanceladosByRemessa(numero);
	    
	    ArquivoRemessa ar = new ArquivoRemessa(numero,mBoletos,horaDoArquivo);
	    ar.grava(writer);
	    
	    writer.flush();
	    writer.close();
	    fc.responseComplete(); 
	}	
	
	
	public String getQtdeBoletos(String numero){
		return ""+mBoletoDao.findNaoCanceladosByRemessa(numero).size();
	}
	
	
	public List<Remessa> getRemessas() {
        return remessas;
	}
	
	private void setFiltrosAcesso() {
	}
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		try {
			setFilters();
			
			remessas = remessaDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = remessaDao.countAll(filters);
		} catch (Exception e) {
			throw new RuntimeException(e); // Handle it yourself.
		}
		lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage==0)
			currentPage=1;
	}
	
	private String numero;
	//private Date abertura;
	//private Date fechamento;
	
	
	@Override
	public void setFilters() {
		preFilters.put("numero",numero);
		normalizeFilters();
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}




	public void setRemessas(List<Remessa> remessas) {
		this.remessas = remessas;
	}


	public boolean isFechamentoReadOnly() {
		return fechamentoReadOnly;
	}


	public void setFechamentoReadOnly(boolean fechamentoReadOnly) {
		this.fechamentoReadOnly = fechamentoReadOnly;
	}

	

}
