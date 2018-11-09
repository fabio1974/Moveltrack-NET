package net.moveltrack.backgroundtasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.dao.Location2Dao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Location2;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.util.MapaUtil;
import net.moveltrack.util.Task;


@ApplicationScoped
public class TaskCorrigeVelocidadeGlobalStar extends Task {
	
	 
	    
		@Inject
		Location2Dao location2Dao;
		
		@Inject
		LocationDao locationDao;
		
		@Inject 
		EquipamentoDao equipamentoDao;
		
		@Inject 
		VeiculoDao veiculoDao;
	    
		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskCorrigeVelocidadeGlobalStar() {
		}
	    

	    @Override
	    public void run() {
			try{

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				
		    	Calendar c = Calendar.getInstance();
		    	c.add(Calendar.HOUR_OF_DAY,-4);
		    	c.add(Calendar.MINUTE,-15);
		    	Date inicio = c.getTime();
		    	
		    	Calendar c1 = Calendar.getInstance();
		    	c1.add(Calendar.HOUR_OF_DAY,-3);
		    	Date fim = c1.getTime();
		    	
		    	List<Equipamento> eqs = equipamentoDao.findByModelo(ModeloRastreador.SPOT_TRACE);
		    	for (Equipamento equipamento : eqs) {

		    		

		    		List<Location> locs = locationDao.getLocationsFromEquipamento(equipamento,inicio,fim);
		    		for (Location loc : locs) {
						double newSpeed = locationDao.getGSSpeed(loc);
						if(newSpeed > 0 && loc.getVelocidade()!= newSpeed) {
							System.out.println(sdf.format(loc.getDateLocation())+ " - Atualizando velocidade imei="+ equipamento.getImei() + " - id="+ loc.getId()+ " - antiga="+loc.getVelocidade() + " - nova="+newSpeed);
							loc.setVelocidade(newSpeed);
							locationDao.merge(loc);
						}
					}
		    		
		    		
		    		List<Location2> locs2 = location2Dao.getLocations2FromEquipamento(equipamento,inicio,fim);
		    		for (Location2 loc : locs2) {
						double newSpeed = location2Dao.getGSSpeed(loc);
						if(newSpeed > 0 && loc.getVelocidade()!= newSpeed) {
							System.out.println(sdf.format(loc.getDateLocation())+ " - Atualizando velocidade imei="+ equipamento.getImei() + " - id="+ loc.getId()+ " - antiga="+loc.getVelocidade() + " - nova="+newSpeed);
							loc.setVelocidade(newSpeed);
							location2Dao.merge(loc);
						}
					}
					
				}
		    	
		    	System.out.println("PASSAGEM DE CORRECAO DE VELOCIDADE DO SPOT3 CONCLUIDA: PERIODO DE "+ sdf.format(inicio) + " ATÉ " + sdf.format(fim));
				
			}catch(Exception e){
				e.printStackTrace();
			}
	    }

	    

	}