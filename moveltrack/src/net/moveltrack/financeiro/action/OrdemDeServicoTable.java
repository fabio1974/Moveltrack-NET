package net.moveltrack.financeiro.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.action.TableBean;
import net.moveltrack.dao.OrdemDeServicoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.OrdemDeServico;
import net.moveltrack.domain.OrdemDeServicoStatus;
import net.moveltrack.domain.OrdemDeServicoTipo;
import net.moveltrack.domain.Veiculo;



@Named
@SessionScoped
public class OrdemDeServicoTable extends TableBean {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	OrdemDeServicoDao ordemDeServicoDao;
	
	List<OrdemDeServico> ordemDeServicos;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "numero";
        //sortField = "dataVencimento";
        totalRows = ordemDeServicoDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setFiltrosAcesso();
		refreshPage();
	}
	
	
	public List<OrdemDeServico> getBoletos() {
        return ordemDeServicos;
	}
	
	private void setFiltrosAcesso() {
	}
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		try {
			setFilters();
			
			ordemDeServicos = ordemDeServicoDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = ordemDeServicoDao.countAll(filters);
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
	private OrdemDeServicoStatus status;
	private OrdemDeServicoTipo servico;
	private Cliente cliente;
	private Veiculo veiculo;
	private Empregado operador;
	
	
	@Override
	public void setFilters() {
		preFilters.put("numero",numero);
		preFilters.put("status",status);
		preFilters.put("servico",servico);
		preFilters.put("clienteId",cliente);
		preFilters.put("veiculoId",veiculo);
		preFilters.put("operadorId",operador);
		normalizeFilters();
	}

	
	
	
	public OrdemDeServicoStatus[] getStatuses(){
		return OrdemDeServicoStatus.values();
	}
	
	public OrdemDeServicoTipo[] getServicos(){
		return OrdemDeServicoTipo.values();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public List<OrdemDeServico> getOrdemDeServicos() {
		return ordemDeServicos;
	}


	public void setOrdemDeServicos(List<OrdemDeServico> ordemDeServicos) {
		this.ordemDeServicos = ordemDeServicos;
	}


	public String getNumero() {
		return numero;
	}
	
	


	public Veiculo getVeiculo() {
		return veiculo;
	}


	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public OrdemDeServicoStatus getStatus() {
		return status;
	}


	public void setStatus(OrdemDeServicoStatus status) {
		this.status = status;
	}


	public OrdemDeServicoTipo getServico() {
		return servico;
	}


	public void setServico(OrdemDeServicoTipo servico) {
		this.servico = servico;
	}


	public Empregado getOperador() {
		return operador;
	}


	public void setOperador(Empregado operador) {
		this.operador = operador;
	}


	
}
