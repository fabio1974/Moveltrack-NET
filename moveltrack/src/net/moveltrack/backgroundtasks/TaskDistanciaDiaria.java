package net.moveltrack.backgroundtasks;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.DistanciaDiariaDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cerca;
import net.moveltrack.domain.DistanciaDiaria;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Viagem;
import net.moveltrack.util.GeoDistanceCalulator;
import net.moveltrack.util.MapaUtil;
import net.moveltrack.util.Task;



@ApplicationScoped
public class TaskDistanciaDiaria extends Task {
	
		@Inject	LocationDao locationDao;
		@Inject ClienteDao clienteDao;
		@Inject VeiculoDao veiculoDao;
		@Inject DistanciaDiariaDao distanciaDiariaDao;
	    
		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskDistanciaDiaria() {
			
		}
	    
		Cerca cerca;
		Viagem viagem;

	    @Override
	    public void run() {
	    	
			Calendar inicio = Calendar.getInstance();
			inicio.add(Calendar.DAY_OF_YEAR,-1);
			inicio.set(Calendar.HOUR_OF_DAY,0);
			inicio.set(Calendar.MINUTE,0);
			inicio.set(Calendar.SECOND,0);

			Calendar fim = Calendar.getInstance();
			fim.add(Calendar.DAY_OF_YEAR,-1);
			fim.set(Calendar.HOUR_OF_DAY,23);
			fim.set(Calendar.MINUTE,59);
			fim.set(Calendar.SECOND,59);
	    	
			try{
				List<Integer> ids = clienteDao.findRelatorioDistanciaDiaria();
				for (Integer id : ids) {
					List<Veiculo> veiculos = veiculoDao.findAtivosByCliente(clienteDao.findById(id));
					for (Veiculo veiculo : veiculos) {
						

						
						List<Object> objs = locationDao.getLocationsFromVeiculo(veiculo,inicio.getTime(),fim.getTime());
							
						if(!objs.isEmpty()) {
								
								List<Location> locs = MapaUtil.otimizaPontosDoBanco(objs,inicio.getTime(),fim.getTime());

								DistanciaDiaria dd = new DistanciaDiaria();
								double distance = getDistance(locs);
								dd.setDataComputada(inicio.getTime());
								dd.setVeiculo(veiculo);
								dd.setDistanciaPercorrida(distance/1.06d);
								
								DistanciaDiaria dd2 = distanciaDiariaDao.findByVeiculoAndDataComputada(veiculo,dd.getDataComputada());
								if(dd2!=null) {
									dd2.setDistanciaPercorrida(dd.getDistanciaPercorrida());
									distanciaDiariaDao.merge(dd2);
								}else
									distanciaDiariaDao.salvar(dd);
						}
						
					}
				}
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
	    }
	    
	    


	    
		

		public double getDistance(List<Location> locs){
			double distance = 0;
			for (int i = 0; i < locs.size()-1; i++) {
				distance = distance + GeoDistanceCalulator.haverSineDistance(locs.get(i+1),locs.get(i)); 
			}
			return distance;
		}
		
		
		
	}