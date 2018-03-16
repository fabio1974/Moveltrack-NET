package net.moveltrack.action.maps;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Contrato;
import net.moveltrack.security.LoginBean;
import net.moveltrack.util.MapFeatures;


@Named
@SessionScoped
public class MapaFrotaBean extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;
	
	@Inject ClienteDao clienteDao;
	@Inject ContratoDao contratoDao;
	
	@Inject 
	LoginBean loginBean;
	
	private Contrato contrato;
	
	MapFeatures  mapFeatures;

	public MapaFrotaBean() {

	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... "+this.getClass().getName());
		if(loginBean.isCliente()){
			Cliente cliente = (Cliente)loginBean.getPessoaLogada();
			setContrato(contratoDao.findByCliente(cliente));
		}
	}


	
	
	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public MapFeatures getMapFeatures() {
		return mapFeatures;
	}

	public void setMapFeatures(MapFeatures mapFeatures) {
		this.mapFeatures = mapFeatures;
	}
}