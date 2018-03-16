package net.moveltrack.financeiro.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.action.TableBean;
import net.moveltrack.dao.ConfigDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.Config;
import net.moveltrack.domain.MBoleto;




@Named
@SessionScoped
public class ConfigTable extends TableBean {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	ConfigDao configDao;
	
	@Inject 
	MBoletoDao mBoletoDao;
	
	boolean fechamentoReadOnly;
	
	List<Config> configs;
	List<MBoleto> mBoletos ;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = configDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setFiltrosAcesso();
		refreshPage();
	}
	
	

	
	
	public List<Config> getConfigs() {
        return configs;
	}
	
	private void setFiltrosAcesso() {
	}
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		try {
			setFilters();
			
			configs = configDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = configDao.countAll(filters);
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




	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}


	public boolean isFechamentoReadOnly() {
		return fechamentoReadOnly;
	}


	public void setFechamentoReadOnly(boolean fechamentoReadOnly) {
		this.fechamentoReadOnly = fechamentoReadOnly;
	}

	

}
