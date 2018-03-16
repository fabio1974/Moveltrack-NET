package net.moveltrack.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.DespesaFrotaDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.DespesaFrota;
import net.moveltrack.domain.DespesaFrotaCategoria;
import net.moveltrack.domain.DespesaFrotaEspecie;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Viagem;

@Named
public class DespesaFrotaTable extends TableBean {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	DespesaFrotaDao despesaFrotaDao;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = despesaFrotaDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setFiltrosAcesso();
        refreshPage();
	}
	
	List<DespesaFrota> despesaFrotas;
	
	public List<DespesaFrota> getDespesaFrotas() {
        return despesaFrotas;
	}
	
	
	private void setFiltrosAcesso() {
		if(loginBean.getPessoaLogada() instanceof Cliente){
			cliente = (Cliente)loginBean.getPessoaLogada();
		}
	}
	
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		System.out.println("refreshing...");
		try {
			setFilters();
			despesaFrotas = despesaFrotaDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = despesaFrotaDao.countAll(filters);
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
		preFilters.put("descricao",descricao);
		preFilters.put("categoria",categoria);
		preFilters.put("especie",especie);
		preFilters.put("veiculoId",veiculo);
		preFilters.put("clienteId",cliente);
		preFilters.put("motoristaId",motorista);
		preFilters.put("viagemId",viagem);
		
		normalizeFilters();
	}

	
	String descricao;
	Veiculo veiculo;
	Motorista motorista;
	Viagem viagem;
	DespesaFrotaCategoria categoria;
	DespesaFrotaEspecie especie;
	Cliente cliente;



	public DespesaFrotaDao getDespesaFrotaDao() {
		return despesaFrotaDao;
	}

	public void setDespesaFrotaDao(DespesaFrotaDao despesaFrotaDao) {
		this.despesaFrotaDao = despesaFrotaDao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public Viagem getViagem() {
		return viagem;
	}

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}

	public DespesaFrotaCategoria getCategoria() {
		return categoria;
	}

	public void setCategoria(DespesaFrotaCategoria categoria) {
		this.categoria = categoria;
	}

	public DespesaFrotaEspecie getEspecie() {
		return especie;
	}
	
	

	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public void setEspecie(DespesaFrotaEspecie especie) {
		this.especie = especie;
	}

	public void setDespesaFrotas(List<DespesaFrota> despesaFrotas) {
		this.despesaFrotas = despesaFrotas;
	}
	
	public DespesaFrotaEspecie[] getEspecies(){
		return DespesaFrotaEspecie.values();
	}
	
	DespesaFrota lastDespesaFrota;

	public DespesaFrota getLastDespesaFrota() {
		this.lastDespesaFrota = despesaFrotaDao.findLastByCliente(cliente);
		return this.lastDespesaFrota;
	}

	public void setLastDespesaFrota(DespesaFrota lastDespesaFrota) {
		this.lastDespesaFrota = lastDespesaFrota;
	}
	
}
