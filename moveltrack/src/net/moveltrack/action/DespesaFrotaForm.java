package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.DespesaFrotaDao;
import net.moveltrack.dao.MotoristaDao;
import net.moveltrack.dao.PerfilDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.DespesaFrota;
import net.moveltrack.domain.DespesaFrotaCategoria;
import net.moveltrack.domain.DespesaFrotaEspecie;
import net.moveltrack.domain.DespesaStatus;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.Permission;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.security.LoginBean;


@Named
@SessionScoped
public class DespesaFrotaForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	DespesaFrotaDao dao;
	
	@Inject
	PessoaDao pessoaDao;

	private DespesaFrota despesaFrota;
	private String action = Action.INSERT; 
	


	public DespesaFrotaForm() {
		System.out.println("Constructor ... "+this.getClass().getCanonicalName());
	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... "+this.getClass().getCanonicalName());
		if(despesaFrota==null)
			buildNewObject();
	}
	
	private void buildNewObject(){
		despesaFrota = new DespesaFrota();
		despesaFrota.setDataDaDespesa(new Date());
		//despesaFrota.setDataDePagamento(new Date());
	}
	

	
	@Inject 
	PerfilDao perfilDao;
	
	@Transactional
	public String excluir() {
		dao.remover(despesaFrota.getId());
		despesaFrotaTable.refreshPage();
		return "despesaFrotaTable";
	}
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			
			if(despesaFrota.getCategoria()==DespesaFrotaCategoria.VIAGEM){
				despesaFrota.setMotorista(null);
				despesaFrota.setVeiculo(null);
			}else if(despesaFrota.getCategoria()==DespesaFrotaCategoria.MOTORISTA){
				despesaFrota.setViagem(null);
				despesaFrota.setVeiculo(null);
			}else if(despesaFrota.getCategoria()==DespesaFrotaCategoria.VEICULO){
				despesaFrota.setMotorista(null);
				despesaFrota.setViagem(null);
			}
			
			if(loginBean.getPessoaLogada() instanceof Cliente)
				despesaFrota.setCliente((Cliente)loginBean.getPessoaLogada());
			
			if(action.contains(Action.INSERT)){
				dao.salvar(despesaFrota);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				dao.merge(despesaFrota);
				operacaoSucesso();
			}
			despesaFrotaTable.refreshPage();
			return "despesaFrotaTable";
		}else
			return "despesaFrotaForm";
	}
	
	@Inject
	DespesaFrotaTable despesaFrotaTable;
	
	

	public String sair(){
		return "despesaFrotaTable";
	}
	
	private Boolean validaGravacao() {
		if (despesaFrota == null) {
			operacaoErro("error.despesaFrota.null");
			return false;
		}
		


		if (despesaFrota.getCategoria()==null) {
			operacaoErro("error.despesaFrota.categoria.nao.informado","categoria", false);
			return false;
		}
		

		if (despesaFrota.getEspecie()==null) {
			operacaoErro("error.despesaFrota.especie.nao.informado","especie", false);
			return false;
		}
		
		if (despesaFrota.getValor()<=0) {
			operacaoErro("error.despesaFrota.valor.nao.informado","valor", false);
			return false;
		}
		
		if (despesaFrota.getCategoria()!=DespesaFrotaCategoria.VIAGEM && despesaFrota.getDataDaDespesa()==null) {
			operacaoErro("error.despesaFrota.dataDaDespesa.nao.informado","dataDaDespesa", false);
			return false;
		}
		
		/*if (despesaFrota.getCategoria()!=DespesaFrotaCategoria.VIAGEM && despesaFrota.getDataDePagamento()==null) {
			operacaoErro("error.despesaFrota.dataDePagamento.nao.informado","dataDePagamento", false);
			return false;
		}*/
		
		if (despesaFrota.getCategoria()==DespesaFrotaCategoria.VIAGEM && despesaFrota.getViagem()==null) {
			operacaoErro("error.despesaFrota.viagem.nao.informado","viagem", false);
			return false;
		}
		
		if (despesaFrota.getCategoria()==DespesaFrotaCategoria.MOTORISTA && despesaFrota.getMotorista()==null) {
			operacaoErro("error.despesaFrota.motorista.nao.informado","motorista", false);
			return false;
		}	
		
		
		if (despesaFrota.getCategoria()==DespesaFrotaCategoria.VEICULO && despesaFrota.getVeiculo()==null) {
			operacaoErro("error.despesaFrota.veiculo.nao.informado","veiculo", false);
			return false;
		}
		
		

		
		

		
		
		
	
		


		
		

		
		if (despesaFrota.getEspecie()==DespesaFrotaEspecie.COMBUSTIVEL && despesaFrota.getLitros()<=0) {
			operacaoErro("error.despesaFrota.litros.nao.informado","litros", false);
			return false;
		}
		

		if (StringUtils.isEmpty(despesaFrota.getDescricao()) && despesaFrota.getEspecie()==DespesaFrotaEspecie.OUTROS) {
			operacaoErro("error.despesaFrota.descricao.nao.informado","descricao", false);
			return false;
		}
		
		if (!StringUtils.isEmpty(despesaFrota.getDescricao()) && despesaFrota.getDescricao().length()>=30) {
			operacaoErro("error.despesaFrota.descricao.maior.que.trinta","descricao", false);
			return false;
		}
		
		
		
		return true;
	}
	

	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		
		if(action.equals(Action.SMARTINSERT)){
			updateEspecies();
			DespesaFrota aux = despesaFrota;
			buildNewObject();
			despesaFrota.setCategoria(aux.getCategoria());
			if(aux.getCategoria()==DespesaFrotaCategoria.VEICULO)
				despesaFrota.setVeiculo(aux.getVeiculo());
			if(aux.getCategoria()==DespesaFrotaCategoria.MOTORISTA)
				despesaFrota.setMotorista(aux.getMotorista());
			if(aux.getCategoria()==DespesaFrotaCategoria.VIAGEM)
				despesaFrota.setViagem(aux.getViagem());
			
			int m = 0;
			m++;
		}
		this.action = action;
	}


	public DespesaFrota getDespesaFrota() {
		return despesaFrota;
	}

	public void setDespesaFrota(DespesaFrota despesaFrota) {
		this.despesaFrota = despesaFrota;
	}

	public DespesaStatus[] getStatuses(){
		return DespesaStatus.values();
	}
	
	public DespesaFrotaCategoria[] getCategorias(){
		return DespesaFrotaCategoria.values();
	}
	
	DespesaFrotaEspecie[] especies= new DespesaFrotaEspecie[]{} ;
	
	public DespesaFrotaEspecie[] getEspecies() {
		return especies;
	}

	public void setEspecies(DespesaFrotaEspecie[] especies) {
		this.especies = especies;
	}
	

	public void updateEspecies(){
		if(despesaFrota.getCategoria()==null)
			return;
		
		switch (despesaFrota.getCategoria()) {
		case MOTORISTA:
			especies = new DespesaFrotaEspecie[]{DespesaFrotaEspecie.TRABALHISTAS,DespesaFrotaEspecie.OUTROS};
			break;
			
		case VEICULO:
			especies = new DespesaFrotaEspecie[]{
					DespesaFrotaEspecie.COMBUSTIVEL,
					DespesaFrotaEspecie.IPVA,
					DespesaFrotaEspecie.LICENCIAMENTO,
					DespesaFrotaEspecie.MANUTENCAO,
					DespesaFrotaEspecie.MULTA_DE_TRANSITO,
					DespesaFrotaEspecie.SEGURO_OBRIGATORIO,
					DespesaFrotaEspecie.OUTROS
					};
			break;
			
		case VIAGEM:
			especies = new DespesaFrotaEspecie[]{DespesaFrotaEspecie.COMBUSTIVEL,
												 DespesaFrotaEspecie.ESTIVA,
												 DespesaFrotaEspecie.DIARIA,
												 DespesaFrotaEspecie.OUTROS};
			break;

		default:
			break;
		}
	}
	
	@Inject
	VeiculoDao veiculoDao;
	
	@Inject 
	LoginBean loginBean;
	
	
	public List<Veiculo> getVeiculos(){
			Pessoa pessoa = loginBean.getPessoaLogada();
			if(pessoa instanceof Cliente)
				return veiculoDao.findByCliente((Cliente)pessoa);
			else if(loginBean.hasPermission(Permission.VEICULO_VER_TODOS))
				return veiculoDao.findAll();
			return null;
	}
	
	@Inject 
	MotoristaDao motoristaDao;
	
	public List<Motorista> getMotoristas(){
		Pessoa pessoa = loginBean.getPessoaLogada();
		if(pessoa instanceof Cliente)
			return motoristaDao.findByCliente((Cliente)pessoa);
		else if(loginBean.hasPermission(Permission.VEICULO_VER_TODOS))
			return motoristaDao.findAll();
		return null;
	}


}