package net.moveltrack.action.maps;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.action.Action;
import net.moveltrack.action.ConversationBaseAction;
import net.moveltrack.action.ViagemTable;
import net.moveltrack.dao.MotoristaDao;
import net.moveltrack.dao.PerfilDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.dao.ViagemDao;
import net.moveltrack.domain.Cerca;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.Permission;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Viagem;
import net.moveltrack.domain.ViagemStatus;
import net.moveltrack.security.LoginBean;


@Named
@ConversationScoped
public class MapaViagemBean extends ConversationBaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	ViagemDao dao;
	
	@Inject
	PessoaDao pessoaDao;
	
	@Inject 
	PerfilDao perfilDao;
	
	@Inject
	ViagemTable viagemTable;

	@Inject
	VeiculoDao veiculoDao;
	
	@Inject 
	LoginBean loginBean;
	
	@Inject 
	MotoristaDao motoristaDao;
	

	private Viagem viagem;
	private Date inicio;
	private Date fim;

 

	public MapaViagemBean() {

	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... "+this.getClass().getName());
	}
	

	public Date getInicio() {
		return inicio;
	}
	
	

	public Date getFim() {
		if(viagem.getStatus()==ViagemStatus.ENCERRADA)
			this.fim = viagem.getChegadaReal();
		else{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR,1);
			this.fim = c.getTime();
		}
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
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
	
	public List<Veiculo> getVeiculos(){
			Pessoa pessoa = loginBean.getPessoaLogada();
			if(pessoa instanceof Cliente)
				return veiculoDao.findAtivosByCliente((Cliente)pessoa);
			else if(loginBean.hasPermission(Permission.VEICULO_VER_TODOS))
				return veiculoDao.findAllOrderBy("placa");
			return null;
	}
	
	
	
	public List<Motorista> getMotoristas(){
		Pessoa pessoa = loginBean.getPessoaLogada();
		if(pessoa instanceof Cliente)
			return motoristaDao.findAtivosByCliente((Cliente)pessoa);
		else if(loginBean.hasPermission(Permission.VEICULO_VER_TODOS))
			return motoristaDao.findAllOrderBy("nome");
		return null;
	}


}