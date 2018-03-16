package net.moveltrack.action.maps;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Viagem;

@Named
@RequestScoped
public class VeiculoRequest implements Serializable {
	
	private static final long serialVersionUID = -1055359889210217409L;

	@Inject
	VeiculoDao veiculoDao;
	
	@Inject 
	MapaVeiculoBean mapaVeiculoBean;
	
	Veiculo veiculo;
	
	@PostConstruct
	public void init() {
		veiculo = mapaVeiculoBean.getVeiculo();  
	}

	public void updateValues() {
	}

	
}
