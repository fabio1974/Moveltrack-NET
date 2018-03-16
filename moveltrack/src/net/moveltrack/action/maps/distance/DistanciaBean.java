package net.moveltrack.action.maps.distance;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Veiculo;


@Named
@ViewScoped
public class DistanciaBean implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

    @Inject 
    LocationDao locationDao;
    
    @Inject 
    VeiculoDao veiculoDao;
	

	@PostConstruct
	public void init() {
		System.out.println(" Init ... "+ this.getClass().getName());
		
		Calendar inicio = Calendar.getInstance();
		inicio.set(2016,3,20,0,0);
		
		Calendar fim = Calendar.getInstance();
		fim.set(2016,3,22,0,0);
		
		Veiculo veiculo = veiculoDao.findByPlaca("NHL-3832");

		List<Object> locs = locationDao.getLocationsFromVeiculo(veiculo, inicio.getTime(),fim.getTime());
		
	}
	
	
	


}