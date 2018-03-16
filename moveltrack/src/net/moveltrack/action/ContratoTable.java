package net.moveltrack.action;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ContratoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.ContratoGeraCarne;
import net.moveltrack.domain.ContratoStatus;
import net.moveltrack.domain.Permission;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.security.LoginBean;

@Named
@SessionScoped
public class ContratoTable extends TableBeanSession {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	ContratoDao contratoDao;
	
	@Inject
	LoginBean loginBean;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = contratoDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;//lastPage>0?lastPage:1;
        //sortAscending = true;
        //filtersKeys = new String[] {"cliente","status","inicio"};
        setFiltrosAcesso();
		refreshPage();
	}
	
	List<Contrato> contratos;
	
	public List<Contrato> getContratos() {
        return contratos;
	}	
	
	private void setFiltrosAcesso() {
		if(loginBean.hasPermission(Permission.CONTRATO_VER_PROPRIO)){
			try{
				numeroContrato = contratoDao.findByUsuario(loginBean.getUsuario()).getNumeroContrato();
			} catch (Exception e) {
				System.out.println("ERRO FILTRO!");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		System.out.println("refreshing...");
		try {
			setFilters();
			contratos = contratoDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = contratoDao.countAll(filters);
		} catch (Exception e) {
			throw new RuntimeException(e); // Handle it yourself.
		}
		lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage==0)
			currentPage=1;
	}
	
	
	@Override
	public void setFilters() {
		preFilters.put("numeroContrato",numeroContrato);
		preFilters.put("contratoGeraCarne",contratoGeraCarne);
		preFilters.put("clienteId",cliente);
		preFilters.put("vendedorId",vendedor);
		preFilters.put("status",status);
		preFilters.put("inicio",inicio);
		normalizeFilters();
		setFiltrosAcesso();
	}
	
	private ContratoGeraCarne contratoGeraCarne;
	String numeroContrato;
	Pessoa vendedor;
	Cliente cliente;
	ContratoStatus status;
	Date inicio;
	
	
	
	
	public ContratoGeraCarne getContratoGeraCarne() {
		return contratoGeraCarne;
	}
	
	public ContratoGeraCarne[] getContratoGeraCarnes(){
		return ContratoGeraCarne.values();
	}

	public void setContratoGeraCarne(ContratoGeraCarne contratoGeraCarne) {
		this.contratoGeraCarne = contratoGeraCarne;
	}

	public Pessoa getVendedor() {
		return vendedor;
	}

	public void setVendedor(Pessoa vendedor) {
		this.vendedor = vendedor;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ContratoStatus getStatus() {
		return status;
	}

	public void setStatus(ContratoStatus status) {
		this.status = status;
	}

	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	
	

	
}
