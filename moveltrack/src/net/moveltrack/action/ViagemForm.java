package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.backgroundtasks.TaskAtualizaViagem;
import net.moveltrack.dao.MotoristaDao;
import net.moveltrack.dao.PerfilDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.dao.ViagemDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.Permission;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Viagem;
import net.moveltrack.domain.ViagemStatus;
import net.moveltrack.security.LoginBean;



@Named
public class ViagemForm extends ConversationBaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	ViagemDao dao;
	
	@Inject
	PessoaDao pessoaDao;

	private Viagem viagem;
	private String action = Action.INSERT; 
	private boolean distanciaPercorridaReadOnly;


	public ViagemForm() {
		System.out.println("Constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... ");
		if(viagem==null)
			buildNewObject();
	}
	
	private void buildNewObject(){
		Pessoa pessoa = loginBean.getPessoaLogada();
		
		viagem = new Viagem();
		if(pessoa instanceof Cliente){
			viagem.setCidadeOrigem(pessoa.getMunicipio());
			viagem.setCliente((Cliente)pessoa);
			viagem.setPartida(new Date());
			viagem.setStatus(ViagemStatus.ABERTA);
		}	
		
	}
	

	
	@Inject 
	PerfilDao perfilDao;
	
	@Inject 
	TaskAtualizaViagem taskAtualizaViagem;
	
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			
			//IF para correção de viagens passadas
			if(viagem.getChegadaPrevista().before(new Date())){
				if(viagem.getChegadaReal()==null && viagem.getStatus()==ViagemStatus.ABERTA){      
					viagem.setChegadaReal(viagem.getChegadaPrevista());
					viagem.setStatus(ViagemStatus.ENCERRADA);
				}
				taskAtualizaViagem.atualizaDistanciaFinalizada(viagem);
			}	

			
			if(action.equals(Action.INSERT)){
				dao.salvar(viagem);
				viagem.setNumeroViagem(viagem.getId());
				dao.merge(viagem);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				viagem.setNumeroViagem(viagem.getId());
				dao.merge(viagem);
				operacaoSucesso();
			}
			viagemTable.refreshPage();
			return "viagemTable";
		}else
			return "viagemForm";
	}
	
	@Inject
	ViagemTable viagemTable;
	
	

	public String sair(){
		return "viagemTable";
	}
	
	
	
	@Transactional
	public String excluir() {
		dao.remover(viagem);
		viagemTable.refreshPage();
		return "viagemTable";
	}
	
	private Boolean validaGravacao() {
		if (viagem == null) {
			operacaoErro("error.viagem.null");
			return false;
		}
		

		if (viagem.getCidadeOrigem()==null) {
			operacaoErro("error.viagem.cidadeOrigem.nao.informado","cidadeOrigem", false);
			return false;
		}
		
		if (viagem.getCidadeDestino()==null) {
			operacaoErro("error.viagem.cidadeDestino.nao.informado","cidadeDestino", false);
			return false;
		}
		
		
		if (viagem.getVeiculo()==null) {
			operacaoErro("error.viagem.veiculo.nao.informado","veiculo", false);
			return false;
		}
		
		if (viagem.getMotorista()==null) {
			operacaoErro("error.viagem.motorista.nao.informado","motorista", false);
			return false;
		}	
		
		if (viagem.getPartida()==null) {
			operacaoErro("error.viagem.partida.nao.informado","partida", false);
			return false;
		}

/*		Calendar atraso = Calendar.getInstance();
		atraso.add(Calendar.HOUR_OF_DAY,-10);
		
		if (viagem.getPartida().before(atraso.getTime())) {
			operacaoErro("error.viagem.partida.atrasada","partida", false);
			return false;
		}
*/
		
		
		
		
		if (viagem.getChegadaPrevista()==null) {
			operacaoErro("error.viagem.chegadaPrevista.nao.informado","chegadaPrevista", false);
			return false;
		}
		
		
		if (!viagem.getChegadaPrevista().after(viagem.getPartida())) {
			operacaoErro("error.viagem.chegadaPrevista.antes.da.partida","chegadaPrevista", false);
			return false;
		}

		
/*		if (viagem.getValorDaCarga()<=0) {
			operacaoErro("error.viagem.valorDaCarga.nao.informado","valorDaCarga", false);
			return false;
		}
*/		
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

	public Viagem getViagem() {
		return viagem;
	}
	

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}

	public ViagemStatus[] getStatuses(){
		return ViagemStatus.values();
	}
	
	

	
	public boolean isDistanciaPercorridaReadOnly() {
		distanciaPercorridaReadOnly = (viagem.getStatus() != ViagemStatus.ENCERRADA && !action.equals("SHOW")) || action.equals("SHOW");
		return distanciaPercorridaReadOnly;
	}

	public void setDistanciaPercorridaReadOnly(boolean distanciaPercorridaReadOnly) {
		this.distanciaPercorridaReadOnly = distanciaPercorridaReadOnly;
	}



	@Inject
	VeiculoDao veiculoDao;
	
	@Inject 
	LoginBean loginBean;
	
	
	public List<Veiculo> getVeiculos(){
			Pessoa pessoa = loginBean.getPessoaLogada();
			if(pessoa instanceof Cliente)
				return veiculoDao.findAtivosByCliente((Cliente)pessoa);
			else if(loginBean.hasPermission(Permission.VEICULO_VER_TODOS))
				return veiculoDao.findAllOrderBy("placa");
			return null;
	}
	
	@Inject 
	MotoristaDao motoristaDao;
	
	public List<Motorista> getMotoristas(){
		Pessoa pessoa = loginBean.getPessoaLogada();
		if(pessoa instanceof Cliente)
			return motoristaDao.findAtivosByCliente((Cliente)pessoa);
		else if(loginBean.hasPermission(Permission.VEICULO_VER_TODOS))
			return motoristaDao.findAllOrderBy("nome");
		return null;
	}


}