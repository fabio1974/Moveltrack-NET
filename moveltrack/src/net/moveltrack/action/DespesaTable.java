package net.moveltrack.action;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.DespesaDao;
import net.moveltrack.domain.Despesa;
import net.moveltrack.domain.DespesaStatus;
import net.moveltrack.domain.DespesaTipo;
import net.moveltrack.domain.Operadora;

@Named
@SessionScoped
public class DespesaTable extends TableBeanSession {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	DespesaDao despesaDao;
	
	List<Despesa> despesas;
	

	
	
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "dataDaDespesa"; 
        totalRows = despesaDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
		refreshPage();
	}
	
	
	
	public List<Despesa> getDespesas() {
        return despesas;
	}	
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		try {
			setFilters();
			
			despesas = despesaDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = despesaDao.countAll(filters);
		} catch (Exception e) {
			throw new RuntimeException(e); // Handle it yourself.
		}
		lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage==0)
			currentPage=1;
	}
	
	private String descricao;
	private DespesaStatus status;
	private DespesaTipo tipoDeDespesa;
	
	@Override
	public void setFilters() {
		preFilters.put("descricao",descricao);
		preFilters.put("status",status);
		preFilters.put("tipoDeDespesa",tipoDeDespesa);
		normalizeFilters();
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public DespesaStatus getStatus() {
		return status;
	}



	public void setStatus(DespesaStatus status) {
		this.status = status;
	}



	public DespesaTipo getTipoDeDespesa() {
		return tipoDeDespesa;
	}



	public void setTipoDeDespesa(DespesaTipo tipoDeDespesa) {
		this.tipoDeDespesa = tipoDeDespesa;
	}



	public void setDespesas(List<Despesa> despesas) {
		this.despesas = despesas;
	}
	

	
	
	

	


	
	
}
