package net.moveltrack.financeiro.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.action.TableBean;
import net.moveltrack.dao.LancamentoDao;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.Lancamento;
import net.moveltrack.domain.LancamentoTipo;
import net.moveltrack.domain.LancamentoStatus;
import net.moveltrack.domain.OrdemDeServico;





@Named
@SessionScoped
public class LancamentoTable extends TableBean {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	LancamentoDao lancamentoDao;
	
	List<Lancamento> lancamentos;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id";
        //sortField = "dataVencimento";
        totalRows = lancamentoDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setFiltrosAcesso();
		refreshPage();
	}
	
	
	public List<Lancamento> getLancamentos() {
        return lancamentos;
	}
	
	private void setFiltrosAcesso() {
	}
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		try {
			setFilters();
			
			lancamentos = lancamentoDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = lancamentoDao.countAll(filters);
		} catch (Exception e) {
			throw new RuntimeException(e); // Handle it yourself.
		}
		lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage==0)
			currentPage=1;
	}
	
	private LancamentoTipo operacao;
	private LancamentoStatus status;
	private OrdemDeServico ordemDeServico;
	private Empregado operador;
	private Empregado solicitante;
	
	
	@Override
	public void setFilters() {
		preFilters.put("operacao",operacao);
		preFilters.put("status",status);
		preFilters.put("ordemDeServicoId",ordemDeServico);
		preFilters.put("operadorId",operador);
		preFilters.put("solicitanteId",solicitante);
		normalizeFilters();
	}

	
	

	
	public LancamentoStatus[] getStatuses(){
		return LancamentoStatus.values();
	}
	

	

	public LancamentoTipo[] getOperacoes(){
		return LancamentoTipo.values();
	}


	public LancamentoTipo getOperacao() {
		return operacao;
	}


	public void setOperacao(LancamentoTipo operacao) {
		this.operacao = operacao;
	}





	public OrdemDeServico getOrdemDeServico() {
		return ordemDeServico;
	}


	public void setOrdemDeServico(OrdemDeServico ordemDeServico) {
		this.ordemDeServico = ordemDeServico;
	}


	public Empregado getOperador() {
		return operador;
	}


	public void setOperador(Empregado operador) {
		this.operador = operador;
	}


	public Empregado getSolicitante() {
		return solicitante;
	}


	public void setSolicitante(Empregado solicitante) {
		this.solicitante = solicitante;
	}


	public LancamentoStatus getStatus() {
		return status;
	}


	public void setStatus(LancamentoStatus status) {
		this.status = status;
	}

	
	
	
}
