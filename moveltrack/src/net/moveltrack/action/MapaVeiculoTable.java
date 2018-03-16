package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.VeiculoDao;
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
public class MapaVeiculoTable extends TableBeanSession{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3707943387912165242L;

	@Inject
	VeiculoDao veiculoDao;
	
	@Inject 
	protected LoginBean loginBean;

	
	@PostConstruct
	public void init() {
		System.out.println("init "+this.getClass().getName());
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = veiculoDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setPreFilters();
        refreshPage();
	}
	
	List<Veiculo> veiculos;

	public List<Veiculo> getVeiculosAtivos() {
        return veiculos;
	}	

	@Override
	public void refreshPage() {
		System.out.println("refreshingPage...");
		int firstRow = (currentPage - 1) * rowsPerPage;
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
	
	private void setPreFilters() {
		setStatus(VeiculoStatus.ATIVO);
		if(loginBean.hasPermission(Permission.VEICULO_VER_PROPRIO)){
			contrato = contratoDao.findByUsuario(loginBean.getUsuario());
		}
	}
	

	@Override
	public void setFilters() {
		preFilters.clear();
		preFilters.put("contratoId",contrato);
		preFilters.put("placa",placa);
		preFilters.put("marcaModelo",marcaModelo);
		preFilters.put("cor",cor);
		preFilters.put("instaladorId",instalador);
		preFilters.put("dataInstalacao",dataInstalacao);
		preFilters.put("equipamento",equipamento);
		preFilters.put("tipo",tipo);
		preFilters.put("status",status);
		normalizeFilters();
	}


	private Contrato contrato;
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

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
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
	
	public Cor[] getCores(){
		return Cor.values();
	}
}
