package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ChipDao;
import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.domain.Chip;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.EquipamentoSituacao;
import net.moveltrack.domain.ModeloRastreador;


@Named
@SessionScoped
public class EquipamentoForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	EquipamentoDao dao;

	private Equipamento equipamento;
	private String action = Action.INSERT; 


	public EquipamentoForm() {
		System.out.println("Constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... ");
		if(equipamento==null)
			buildNewObject();
	}
	
	private void buildNewObject(){
		equipamento = new Equipamento();
	}
	
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			equipamento.setUltimaAlteracao(new Date());
			if(action.equals(Action.INSERT)){
				dao.salvar(equipamento);
				equipamento.setSenha(String.valueOf(1000010-equipamento.getId()));
				dao.merge(equipamento);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				//if(equipamento.getSenha()==null)
					//equipamento.setSenha(String.valueOf(1000010-equipamento.getId()));
				dao.merge(equipamento);					
				operacaoSucesso();
			}
			equipamentoTable.refreshPage();
			return "equipamentoTable";
		}else
			return "equipamentoForm";
	}
	
	@Inject
	EquipamentoTable equipamentoTable;
	

	public String sair(){
		equipamentoTable.refreshPage();
		return "equipamentoTable";
	}
	
	private Boolean validaGravacao() {
		if (equipamento.getImei() == null || equipamento.getImei().equals("")) {
			operacaoErro("error.equipamento.imei.nao.informado","imei",false);
			return false;
		}
		
		if (isImeiDuplicado(equipamento.getImei())) {
			operacaoErro("error.equipamento.imei.duplicado","imei",false);
			return false;
		}
		

		if (equipamento.getChip()!=null && isChipDuplicado(equipamento.getChip().getIccid())) {
			operacaoErro("error.equipamento.chip.iccid.duplicado","chip",false);
			return false;
		}


		
		if (equipamento.getModelo() == null) {
			operacaoErro("error.equipamento.modelo.nao.informado","modelo",false);
			return false;
		}
		
		if (equipamento.getSenha() == null) {
			operacaoErro("error.equipamento.senha.nao.informado","modelo",false);
			return false;
		}
		
		if (equipamento.getSenha().length() != 6) {
			operacaoErro("error.equipamento.senha.comprimento.seis","modelo",false);
			return false;
		}
		
		
		if (equipamento.getSituacao() == null) {
			operacaoErro("error.equipamento.situacao.nao.informado","situacao",false);
			return false;
		}
		
		if (equipamento.getDataCadastro() == null && (action.equals(Action.INSERT)||action.equals(Action.SMARTINSERT))) {
			operacaoErro("error.equipamento.dataCadastro.nao.informado","dataCadastro",false);
			return false;
		}
		

		return true;
	}
	
	
	private boolean isImeiDuplicado(String imei) {
		Equipamento doBanco = dao.findByImei(imei);
		return (doBanco!=null && doBanco.getId().intValue()!= equipamento.getId().intValue());
	}
	
	@Inject ChipDao chipDao;
	private boolean isChipDuplicado(String iccid) {
		Equipamento doBanco = dao.findByChipIccid(iccid);
		return (doBanco!=null && doBanco.getId().intValue()!= equipamento.getId().intValue());
	}


	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewObject();
		this.action = action;
	}


	public ModeloRastreador[] getModelos(){
		return ModeloRastreador.values();
	}
	
	public EquipamentoSituacao[] getSituacoes(){
		return EquipamentoSituacao.values();
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}


}