package net.moveltrack.action;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.Cor;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.Permission;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.VeiculoStatus;
import net.moveltrack.domain.VeiculoTipo;
import net.moveltrack.security.LoginBean;

@Named
@SessionScoped
public class VeiculoTable extends TableBeanSession {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	VeiculoDao veiculoDao;

	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = veiculoDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setFiltrosAcesso();
		refreshPage();
	}
	
	List<Veiculo> veiculos;
	
	public List<Veiculo> getVeiculos() {
        return veiculos;
	}
	
	
/*	public List<Veiculo> getVeiculosAtivos() {
		setStatus(VeiculoStatus.ATIVO);
		refreshPage();
        return veiculos;
	}	
*/
	
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		System.out.println("refreshing...");
		try {
			setFilters();
			veiculos = veiculoDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending,filters);
			totalRows = veiculoDao.countAll(filters);
		} catch (Exception e) {
			throw new RuntimeException(e); // Handle it yourself.
		}
		lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage==0)
			currentPage=1;
	}
	
	
	@Inject
	ContratoDao contratoDao;
	
	@Inject ClienteDao clienteDao;
	@Inject UsuarioDao usuarioDao;;
	
	@Inject 
	LoginBean loginBean;
	
	private void setFiltrosAcesso() {
		if(loginBean.hasPermission(Permission.VEICULO_VER_PROPRIO)){
			try {
				cliente = clienteDao.findByUsuario(loginBean.getUsuario());	
				status = VeiculoStatus.ATIVO;
			} catch (Exception e) {
				System.out.println("ERRO FILTRO!");
				e.printStackTrace();
			}
		}
	}
	

	@Override
	public void setFilters() {
		preFilters.clear();
		preFilters.put("placa",placa);
		preFilters.put("contrato.clienteId",cliente);
		preFilters.put("marcaModelo",marcaModelo);
		preFilters.put("cor",cor);
		preFilters.put("instaladorId",instalador);
		preFilters.put("dataInstalacao",dataInstalacao);
		preFilters.put("equipamento",equipamento);
		preFilters.put("tipo",tipo);
		preFilters.put("status",status);
		normalizeFilters();
		setFiltrosAcesso();
	}


	private Cliente cliente;
	private String placa;
	private String marcaModelo;
	private Cor cor;
	private Empregado instalador;
	private Date dataInstalacao;
	private Equipamento equipamento;
	private VeiculoTipo tipo;
	private VeiculoStatus status;
	private String origem;
	
	

	


	public String getOrigem() {
		return origem;
	}


	public void setOrigem(String origem) {
		this.origem = origem;
	}


	public VeiculoStatus getStatus() {
		return status;
	}

	public void setStatus(VeiculoStatus status) {
		this.status = status;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}



	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getMarcaModelo() {
		return marcaModelo;
	}

	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}

	public Cor getCor() {
		return cor;
	}

	public void setCor(Cor cor) {
		this.cor = cor;
	}

	public Empregado getInstalador() {
		return instalador;
	}

	public void setInstalador(Empregado instalador) {
		this.instalador = instalador;
	}

	public Date getDataInstalacao() {
		return dataInstalacao;
	}

	public void setDataInstalacao(Date dataInstalacao) {
		this.dataInstalacao = dataInstalacao;
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	public VeiculoTipo getTipo() {
		return tipo;
	}

	public void setTipo(VeiculoTipo tipo) {
		this.tipo = tipo;
	}
	
	

	
}
