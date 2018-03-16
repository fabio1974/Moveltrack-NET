package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.EmpregadoDao;
import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.Cor;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.PerfilTipo;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.VeiculoStatus;
import net.moveltrack.domain.VeiculoTipo;


@Named
@SessionScoped
public class VeiculoForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	VeiculoDao dao;
	
	@Inject
	EmpregadoDao empregadoDao;
	
	@Inject
	EquipamentoDao equipamentoDao;

	private Veiculo veiculo;
	private String action = Action.INSERT;
	private List<Empregado> instaladores;
	


	public VeiculoForm() {
		System.out.println("Constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... ");
		if(veiculo==null)
			buildNewObject();
		instaladores = empregadoDao.findByTipo(PerfilTipo.INSTALADOR);
		
	}
	
	private void buildNewObject(){
		veiculo = new Veiculo();
		veiculo.setDataInstalacao(new Date());
	}
	
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			veiculo.setPlaca(veiculo.getPlaca().toUpperCase());
			veiculo.setUltimaAlteracao(new Date());
			if(action.contains(Action.INSERT)){
				dao.salvar(veiculo);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				dao.merge(veiculo);
				operacaoSucesso();
			}
			veiculoTable.refreshPage();
			return "veiculoTable";
		}else
			return "veiculoForm";
	}
	
	@Inject
	VeiculoTable veiculoTable;
	

	public String sair(){
		return "veiculoTable";
	}
	
	
	private boolean isPlacaDuplicada(String placa) {
		Veiculo doBanco = dao.findByPlaca(placa);
		return (doBanco!=null && doBanco.getId().intValue()!= veiculo.getId().intValue());
	}
	
	
	private Boolean validaGravacao() {
		if (veiculo.getContrato() == null) {
			operacaoErro("error.veiculo.contrato.nao.informado","contrato",false);
			return false;
		}
		
		if (!StringUtils.isBlank(veiculo.getPlaca()) && isPlacaDuplicada(veiculo.getPlaca())) {
			operacaoErro("error.veiculo.placa.duplicada","placa",false);
			return false;
		}
		
		
		if (StringUtils.isBlank(veiculo.getMarcaModelo())) {
			operacaoErro("error.veiculo.marcaModelo.nao.informado","marcaModelo",false);
			return false;
		}
		
		if (veiculo.getMarcaModelo().length()>25) {
			operacaoErro("error.veiculo.marcaModelo.maior.que","marcaModelo",false);
			return false;
		}
		


		
		if (veiculo.getCor()==null) {
			operacaoErro("error.veiculo.cor.nao.informado","cor",false);
			return false;
		}
		

		if (veiculo.getContrato().getCliente().isPessoaJuridica() && veiculo.getConsumoCombustivel()<=0) {
			operacaoErro("error.veiculo.consumoCombustivel.nao.informado","consumoCombustivel",false);
			return false;
		}

		
		
		if (veiculo.getDataInstalacao()==null) {
			operacaoErro("error.veiculo.dataInstalacao.nao.informado","dataInstalacao",false);
			return false;
		}
		
		if(isRastreadorEmOutroVeiculo()){
			operacaoErro("error.veiculo.equipamento.ja.cadastrado","equipamento",false);
			return false;
		}
			
		
		if (veiculo.getStatus()==null) {
			operacaoErro("error.veiculo.status.nao.informado","status",false);
			return false;
		}
		
		if (veiculo.getTipo()==null) {
			operacaoErro("error.veiculo.tipo.nao.informado","tipo",false);
			return false;
		}

		return true;
	}
	
	
	
	private boolean isRastreadorEmOutroVeiculo() {
		if(veiculo.getEquipamento()==null)
			return false;
		Veiculo v = dao.findByEquipamento(veiculo.getEquipamento().getImei());
		if(v==null)
			return false;
		else
			if((action.equals(Action.UPDATE) && v.getId().intValue()!=veiculo.getId().intValue())||action.contains(Action.INSERT))
				return true;
		return false;
	}
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewObject();
		else if(action.equals(Action.SMARTINSERT)){
			Contrato contrato = veiculo.getContrato();
			buildNewObject();
			veiculo.setContrato(contrato);
		}
		this.action = action;
	}


	public VeiculoTipo[] getTipos(){
		return VeiculoTipo.values();
	}
	
	public VeiculoStatus[] getStatuses(){
		return VeiculoStatus.values();
	}
	
	public Cor[] getCores(){
		return Cor.values();
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public List<Empregado> getInstaladores() {
		return instaladores;
	}

	public void setInstaladores(List<Empregado> instaladores) {
		this.instaladores = instaladores;
	}




}