package net.moveltrack.action;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Chip;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.EquipamentoSituacao;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.Veiculo;

@Named
@SessionScoped
public class EquipamentoTable extends TableBeanSession {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	EquipamentoDao equipamentoDao;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = equipamentoDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;//lastPage>0?lastPage:1;
        //sortAscending = true;
        //filtersKeys = new String[] {"imei","chip","modelo","situacao","senha","dataCadastro"};
		refreshPage();
	}
	
	List<Equipamento> equipamentos;
	
	public List<Equipamento> getEquipamentos() {
        return equipamentos;
	}	
	
	@Inject VeiculoDao veiculoDao;
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		System.out.println("refreshing...");
		try {
			setFilters();
			equipamentos = equipamentoDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = equipamentoDao.countAll(filters);
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
		preFilters.put("imei",imei);
		preFilters.put("senha",senha);
		preFilters.put("chipId",chip);
		preFilters.put("modelo",modelo);
		preFilters.put("possuidorId",possuidor);
		preFilters.put("situacao",situacao);
		preFilters.put("dataCadastro",dataCadastro);
		normalizeFilters();
	}
	
	private String imei;
	private String senha;
	private Chip chip;
	private ModeloRastreador modelo;
	private EquipamentoSituacao situacao;
	private Empregado possuidor;
	private Date dataCadastro;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Chip getChip() {
		return chip;
	}

	public void setChip(Chip chip) {
		this.chip = chip;
	}

	public ModeloRastreador getModelo() {
		return modelo;
	}

	public void setModelo(ModeloRastreador modelo) {
		this.modelo = modelo;
	}

	public EquipamentoSituacao getSituacao() {
		return situacao;
	}

	public void setSituacao(EquipamentoSituacao situacao) {
		this.situacao = situacao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public void setEquipamentos(List<Equipamento> equipamentos) {
		this.equipamentos = equipamentos;
	}

	public Empregado getPossuidor() {
		return possuidor;
	}

	public void setPossuidor(Empregado possuidor) {
		this.possuidor = possuidor;
	}

	
	public String getVeiculo(Equipamento equipamento) {
		Veiculo veiculo = veiculoDao.findByEquipamento(equipamento.getImei());
		String retorno = "";
		if(veiculo!=null)
			retorno = veiculo.getPlaca();
		if(veiculo!=null && veiculo.getContrato()!=null)
			retorno += " / "+ veiculo.getContrato().getCliente().getNome();
		return retorno;
	}
	
}
