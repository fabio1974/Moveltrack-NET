package net.moveltrack.action;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ChipDao;
import net.moveltrack.domain.Chip;
import net.moveltrack.domain.ChipStatus;
import net.moveltrack.domain.Operadora;

@Named
@SessionScoped
public class ChipTable extends TableBeanSession {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	ChipDao chipDao;
	
	List<Chip> chips;
	

	
	
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = chipDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
		refreshPage();
	}
	
	
	
	public List<Chip> getChips() {
        return chips;
	}	
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		try {
			setFilters();
			
			chips = chipDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = chipDao.countAll(filters);
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
	private String iccid;
	private Date dataCadastro;
	private Operadora operadora;
	private ChipStatus status;
	
	@Override
	public void setFilters() {
		preFilters.put("numero",numero);
		preFilters.put("iccid",iccid);
		preFilters.put("dataCadastro",dataCadastro);
		preFilters.put("operadora",operadora);
		preFilters.put("status",status);
		normalizeFilters();
	}
	
	

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Operadora getOperadora() {
		return operadora;
	}

	public void setOperadora(Operadora operadora) {
		this.operadora = operadora;
	}



	public ChipStatus getStatus() {
		return status;
	}



	public void setStatus(ChipStatus status) {
		this.status = status;
	}


	
	
}
