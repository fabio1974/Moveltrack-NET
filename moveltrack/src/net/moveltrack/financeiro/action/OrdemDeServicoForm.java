package net.moveltrack.financeiro.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.action.Action;
import net.moveltrack.action.DespesaTable;
import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.DespesaDao;
import net.moveltrack.dao.OrdemDeServicoDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Despesa;
import net.moveltrack.domain.DespesaStatus;
import net.moveltrack.domain.DespesaTipo;
import net.moveltrack.domain.Operadora;
import net.moveltrack.domain.OrdemDeServico;
import net.moveltrack.domain.OrdemDeServicoStatus;
import net.moveltrack.domain.Remessa;
import net.moveltrack.domain.TipoDeCobranca;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.security.LoginBean;

@Named
@SessionScoped
public class OrdemDeServicoForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	OrdemDeServicoDao dao;

	private OrdemDeServico ordemDeServico;
	private Remessa remessa;
	private String action = Action.INSERT; 
	private List<Veiculo> veiculos;
	
	public OrdemDeServicoForm() {
		System.out.println("ordemDeServico form constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("ordemDeServico form  Init ... ");
		if(ordemDeServico==null)
			buildNewObject();
	}
	
	@Inject LoginBean loginBean;
	
	private void buildNewObject(){
		
		ordemDeServico = new OrdemDeServico();
		ordemDeServico.setDataDoServico(new Date());
		//ordemDeServico.setOperador((Empregado)loginBean.getPessoaLogada());
	}
	
	
	@Inject ContratoDao contratoDao;
	@Inject OrdemDeServicoTable ordemDeServicoTable;
	@Inject DespesaTable despesaTable;
	@Inject DespesaDao despesaDao;
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			if(action.equals(Action.INSERT)){
				
				dao.salvar(ordemDeServico);
				ordemDeServico.setNumero(String.format("%07d",ordemDeServico.getId().intValue()));
				dao.merge(ordemDeServico);
				
				Despesa despesa = new Despesa();
				despesa.setDataDaDespesa(new Date());
				despesa.setDescricao(ordemDeServico.getOperador().getNome()+ "-"+ ordemDeServico.getServico() +" - "+ ordemDeServico.getCliente().getNome() +" - "+ ordemDeServico.getVeiculo().getMarcaModelo());
				despesa.setOsid(ordemDeServico.getId());
				despesa.setStatus(DespesaStatus.PENDENTE);
				despesa.setTipoDeDespesa(DespesaTipo.SERVICOS_DO_INSTALADOR);
				despesa.setValor(ordemDeServico.getValorDoServico());
				despesaDao.salvar(despesa);
				
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				dao.merge(ordemDeServico);
				Despesa despesa = despesaDao.findByOrdemDeServico(ordemDeServico);
				if(despesa != null){
					if(ordemDeServico.getStatus() == OrdemDeServicoStatus.CANCELADA)
						despesa.setStatus(DespesaStatus.CANCELADA);
					else 
						despesa.setStatus(DespesaStatus.PENDENTE);
					despesaDao.merge(despesa);
				}
				operacaoSucesso();
			}
			ordemDeServicoTable.refreshPage();
			despesaTable.refreshPage();
			return "ordemDeServicoTable";
		}else
			return "ordemDeServicoForm";
	}
	

	public String sair(){
		return "ordemDeServicoTable";
	}
	
	private Boolean validaGravacao() {
		

		if (ordemDeServico.getServico()==null) {
			operacaoErro("error.ordemDeServico.servico.nao.informado","cliente",false);
			return false;
		}
		
		if (ordemDeServico.getOperador() == null) {
			operacaoErro("error.ordemDeServico.operador.nao.informado","operador",false);
			return false;
		}

		
		if (ordemDeServico.getDataDoServico() == null) {
			operacaoErro("error.ordemDeServico.dataDoServico.nao.informado","dataDoServico",false);
			return false;
		}

		
		if (ordemDeServico.getCliente() == null) {
			operacaoErro("error.ordemDeServico.cliente.nao.informado","cliente",false);
			return false;
		}
		
		
		if (ordemDeServico.getVeiculo() == null) {
			operacaoErro("error.ordemDeServico.veiculo.nao.informado","veiculo",false);
			return false;
		}

		

		
		if (ordemDeServico.getValorDoServico() <= 0) {
			operacaoErro("error.ordemDeServico.valor.diferente.de.zero","valorDoServico",false);
			return false;
		}

		
		if (StringUtils.isBlank(ordemDeServico.getObservacao())) {
			operacaoErro("error.ordemDeServico.observacao.null","observacao",false);
			return false;
		}

		if (ordemDeServico.getStatus()== null) {
			operacaoErro("error.ordemDeServico.status.nao.informado","status",false);
			return false;
		}
		
		if (ordemDeServico.getServico()== null) {
			operacaoErro("error.ordemDeServico.servico.nao.informado","status",false);
			return false;
		}
		return true;
	}
	
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewObject();
		this.action = action;
	}
	
	@Inject VeiculoDao veiculoDao;

	public List<Veiculo> getVeiculos() {
		if(ordemDeServico.getCliente()!=null)
			veiculos = veiculoDao.findByCliente(ordemDeServico.getCliente());
		else 
			veiculos = new ArrayList<Veiculo>();
		return veiculos;
	}
	
	
	

	public Operadora[] getOperadoras(){
		return Operadora.values();
	}
	
	public TipoDeCobranca[] getTipoDeCobrancas(){
		return TipoDeCobranca.values();
	}

	public OrdemDeServico getBoleto() {
		return ordemDeServico;
	}

	public void setBoleto(OrdemDeServico ordemDeServico) {
		this.ordemDeServico = ordemDeServico;
	}



	public Remessa getRemessa() {
		return remessa;
	}

	public void setRemessa(Remessa remessa) {
		this.remessa = remessa;
	}

	public OrdemDeServico getOrdemDeServico() {
		return ordemDeServico;
	}

	public void setOrdemDeServico(OrdemDeServico ordemDeServico) {
		this.ordemDeServico = ordemDeServico;
	}
	


}