package net.moveltrack.action.maps;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ViagemDao;
import net.moveltrack.domain.Viagem;

@Named
@RequestScoped
public class ViagemRequest implements Serializable {
	
	private static final long serialVersionUID = -1055359889210217409L;

	@Inject
	ViagemDao viagemDao;
	
	@Inject 
	MapaViagemBean mapaViagemBean;
	
	Viagem viagem;
	
	@PostConstruct
	public void init() {
		viagem = mapaViagemBean.getViagem();  
	}
	
	
	public void updateDistanciaPercorrida() {
		Viagem aux = viagemDao.findViagemByNumero(mapaViagemBean.getViagem().getId());
		double distancia = aux.getDistanciaPercorrida();
		System.out.println("-------distancia---------="+distancia);
		viagem.setChegadaReal(aux.getChegadaReal());
		viagem.setStatus(aux.getStatus());
		viagem.setDistanciaPercorrida(distancia);
	}
	
	public boolean 	stop() {
		return viagem.getChegadaReal()!=null && viagem.getChegadaReal().before(new Date());
	}

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}

	public Viagem getViagem() {
		return viagem;
	}
	
}
