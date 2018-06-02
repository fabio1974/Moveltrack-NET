package net.moveltrack.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ViagemDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.Municipio;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Viagem;
import net.moveltrack.domain.ViagemStatus;

@Named
@ConversationScoped
public class ViagemTable extends TableBean {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	ViagemDao viagemDao;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = viagemDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setFiltrosAcesso();
		refreshPage();
	}
	
	List<Viagem> viagens;
	
	public List<Viagem> getViagens() {
        return viagens;
	}	
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		System.out.println("refreshing...");
		try {
			setFilters();
			viagens = viagemDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = viagemDao.countAll(filters);
		} catch (Exception e) {
			throw new RuntimeException(e); // Handle it yourself.
		}
		lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage==0)
			currentPage=1;
	}
	
	
	private void setFiltrosAcesso() {
		if(loginBean.getPessoaLogada() instanceof Cliente){
			cliente = (Cliente)loginBean.getPessoaLogada();
		}
	}

	
	@Override
	public void setFilters() {

		preFilters.put("numeroViagem",numeroViagem);
		preFilters.put("descricao",descricao);
		preFilters.put("status",status);
		preFilters.put("veiculoId",veiculo);
		preFilters.put("clienteId",cliente);
		preFilters.put("motoristaId",motorista);
		preFilters.put("cidadeDestinoId",cidadeDestino);
		
		normalizeFilters();
	}

	
	Integer numeroViagem;
	String descricao;
	Veiculo veiculo;
	ViagemStatus status;
	Motorista motorista;
	Municipio cidadeDestino;
	Cliente cliente;










	public Integer getNumeroViagem() {
		return numeroViagem;
	}

	public void setNumeroViagem(Integer numeroViagem) {
		this.numeroViagem = numeroViagem;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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



	public ViagemStatus getStatus() {
		return status;
	}

	public void setStatus(ViagemStatus status) {
		this.status = status;
	}

	public Municipio getCidadeDestino() {
		return cidadeDestino;
	}

	public void setCidadeDestino(Municipio cidadeDestino) {
		this.cidadeDestino = cidadeDestino;
	}

	public void setViagens(List<Viagem> viagens) {
		this.viagens = viagens;
	}
	

	
}
