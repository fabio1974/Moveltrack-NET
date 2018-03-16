package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ChipDao;
import net.moveltrack.domain.Chip;
import net.moveltrack.domain.ChipStatus;
import net.moveltrack.domain.Operadora;


@Named
@SessionScoped
public class ChipForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	ChipDao dao;

	private Chip chip;
	private String action = Action.INSERT; 


	public ChipForm() {
		System.out.println("chip form constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("chip form  Init ... ");
		if(chip==null)
			buildNewObject();
	}
	
	private void buildNewObject(){
		chip = new Chip();
		chip.setDataCadastro(new Date());
	}
	
	@Inject 
	ChipTable chipTable;
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			chip.setUltimaAlteracao(new Date());
			if(action.equals(Action.INSERT)){
				dao.salvar(chip);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				dao.merge(chip);
				operacaoSucesso();
			}
			chipTable.refreshPage();
			return "chipTable";
		}else
			return "chipForm";
	}
	

	public String sair(){
		chipTable.refreshPage();
		return "chipTable";
	}
	
	private Boolean validaGravacao() {
		if (chip.getNumero() == null || chip.getNumero().equals("") ) {
			operacaoErro("error.chip.numero.nao.informado","numero",false);
			return false;
		}
		
		if(isNumeroChipDuplicado(chip.getNumero())){
			operacaoErro("error.chip.numero.duplicado","numero",false);
			return false;
		}
		
		if(StringUtils.isNotBlank(chip.getIccid())&& isIccidDuplicado(chip.getIccid())){
			operacaoErro("error.chip.iccid.duplicado","iccid",false);
			return false;
		}
		
		if (chip.getOperadora() == null) {
			operacaoErro("error.chip.operadora.nao.informado","operadora",false);
			return false;
		}
		
		
		if (chip.getStatus() == null) {
			operacaoErro("error.chip.status.informado","status",false);
			return false;
		}

		
		
		if (chip.getDataCadastro() == null) {
			operacaoErro("error.chip.dataCadastro.nao.informado","dataCadastro",false);
			return false;
		}

		return true;
	}
	
	
	private boolean isIccidDuplicado(String iccid) {
		Chip doBanco = dao.findByIccid(iccid);
		return (doBanco!=null && doBanco.getId().intValue()!= chip.getId().intValue());
	}

	private boolean isNumeroChipDuplicado(String numero) {
		Chip doBanco = dao.findByNumero(numero);
		return (doBanco!=null && doBanco.getId().intValue()!= chip.getId().intValue());
	}
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewObject();
		this.action = action;
	}


	public Operadora[] getOperadoras(){
		return Operadora.values();
	}
	
	public ChipStatus[] getStatuses(){
		return ChipStatus.values();
	}

	public Chip getChip() {
		return chip;
	}

	public void setChip(Chip chip) {
		this.chip = chip;
	}
	


}