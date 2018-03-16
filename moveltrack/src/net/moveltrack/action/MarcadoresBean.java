package net.moveltrack.action;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.GeoEnderecoDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Veiculo;



@Named
@SessionScoped
public class MarcadoresBean extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	private Veiculo veiculo;


	public MarcadoresBean() {
		System.out.println("MarcadoresBean constructor ... ");
	}

	@PostConstruct
	public void init() {

	}
	
	
	@Transactional
	public void salvar() {
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	
	private Location center;


	@Inject LocationDao locationDao;
	@Inject GeoEnderecoDao geoEnderecoDao;

	public Location getCenter() {
		center = locationDao.getLastLocationFromVeiculo(veiculo);
		if(center == null){
			Cliente cliente = veiculo.getContrato().getCliente();
			if(cliente!=null)
				center = geoEnderecoDao.getLocationFromAddress(cliente.getBairro(),cliente.getMunicipio().getDescricao(),cliente.getMunicipio().getUf().getSigla());
		}
		if(center==null){
			center = new Location();
			center.setLatitude(-3.742117); 
			center.setLongitude(-38.523537);
		}	
		return center;
	}

	public void setCenter(Location center) {
		this.center = center;
	}



	
	
	
	
	
	



}