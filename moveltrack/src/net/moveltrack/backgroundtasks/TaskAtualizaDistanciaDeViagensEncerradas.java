package net.moveltrack.backgroundtasks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.ViagemThreadDao;
import net.moveltrack.domain.Cerca;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Viagem;
import net.moveltrack.domain.ViagemStatus;
import net.moveltrack.util.GeoDistanceCalulator;
import net.moveltrack.util.Task;
import net.moveltrack.util.Utils;



@ApplicationScoped
public class TaskAtualizaDistanciaDeViagensEncerradas extends Task {
	
	    @Inject
	    ViagemThreadDao viagemThreadDao;
	    
		@Inject
		LocationDao locationDao;
	    
		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskAtualizaDistanciaDeViagensEncerradas() {
			
		}
	    
		Cerca cerca;
		Viagem viagem;

	    @Override
	    public void run() {
			try{
				List<Viagem> viagens = viagemThreadDao.findAllJunho();
				System.out.println("------------------------------------------------------------------------");
				System.out.println(" atualizando distância de viagens encerradas " +viagens.size()+" viagens!");
				System.out.println("------------------------------------------------------------------------");

				for (Viagem v : viagens) {
					viagem = v;
					System.out.println("----- atualizando viagem:  " + viagem.getId()+ "------");
					cerca = viagem.getCliente().getCerca();
					atualizaStatus();
					viagemThreadDao.merge(viagem);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
	    }
	    
	    
		private void atualizaStatus() {

			Calendar cFim = Calendar.getInstance();
			cFim.setTime(viagem.getChegadaPrevista());
			cFim.add(Calendar.DAY_OF_YEAR,5);
			
			List<Object> locs = locationDao.getLocationsFromVeiculo(viagem.getVeiculo(),viagem.getPartida(),cFim.getTime());
			
			for (int i = 0; i < locs.size() && viagem.getStatus()!=ViagemStatus.ENCERRADA; i++) {
				Object obj = locs.get(i);
				Location loc = Utils.getLocationFromObject(obj);
				atualizaStatus(loc);
				atualizaDistancia(locs,i);
			}	
				
			if(viagem.getEntrouNaCerca()!=null && viagem.getStatus()!=ViagemStatus.ENCERRADA){
				viagem.setStatus(ViagemStatus.ENCERRADA);
				viagem.setChegadaReal(viagem.getEntrouNaCerca());
			}	
			
			atualizaDistanciaFinalizada(viagem);
		}

		private void atualizaDistancia(List<Object> locs,int i) {
			List<Object> newLocs = locs.subList(0,i);
			double distance = getDistance(newLocs);
			if(distance>0){
				viagem.setDistanciaPercorrida(distance);
			}
		}

		private void atualizaStatus(Location lastLoc) {
			
			Date now = lastLoc.getDateLocation();
			double distancia = GeoDistanceCalulator.haverSineDistance(cerca.getLat1(),cerca.getLon1(),lastLoc.getLatitude(),lastLoc.getLongitude());

			
			
			if(viagem.getDistanciaPercorrida()==0 && viagem.getSaiuDaCerca()==null && isInCerca(distancia,cerca)){
				if(now.getTime() - viagem.getPartida().getTime() > 15*60*1000 && viagem.getStatus()!=ViagemStatus.PARTIDA_EM_ATRASO)  //15 minutos de tolerancia
					viagem.setStatus(ViagemStatus.PARTIDA_EM_ATRASO);
			}else if(viagem.getDistanciaPercorrida()>0 && viagem.getSaiuDaCerca()==null && isInCerca(distancia,cerca) && viagem.getStatus()!=ViagemStatus.SAINDO){
				viagem.setStatus(ViagemStatus.SAINDO);
			}else if(viagem.getDistanciaPercorrida()>0 && viagem.getSaiuDaCerca()==null && !isInCerca(distancia,cerca) && viagem.getStatus()!=ViagemStatus.SAIU_DA_CERCA){
				viagem.setStatus(ViagemStatus.SAIU_DA_CERCA);
				viagem.setSaiuDaCerca(now);
			}else if(viagem.getDistanciaPercorrida()>0 && viagem.getSaiuDaCerca()!=null && viagem.getEntrouNaCerca()==null && !isInCerca(distancia,cerca) && viagem.getStatus()!=ViagemStatus.NA_ESTRADA){
				viagem.setStatus(ViagemStatus.NA_ESTRADA);
			}else if(viagem.getDistanciaPercorrida()>0 && viagem.getSaiuDaCerca()!= null && viagem.getEntrouNaCerca()==null && isInCerca(distancia,cerca) && viagem.getStatus()!=ViagemStatus.ENTROU_NA_CERCA){
				viagem.setStatus(ViagemStatus.ENTROU_NA_CERCA);
				viagem.setEntrouNaCerca(now);
			}else if(viagem.getDistanciaPercorrida()>0 && viagem.getSaiuDaCerca()!= null && viagem.getEntrouNaCerca()!=null && viagem.getChegadaReal()==null && isInCerca(distancia,cerca)){
				if(1000*distancia > 500 && viagem.getStatus()!=ViagemStatus.SE_APROXIMANDO){
					viagem.setStatus(ViagemStatus.SE_APROXIMANDO);
				}else{
					if(now.getTime() - viagem.getChegadaPrevista().getTime() < 15*60*1000 && viagem.getStatus()!=ViagemStatus.CHEGADA_EM_TEMPO)
						viagem.setStatus(ViagemStatus.CHEGADA_EM_TEMPO);
					else if(viagem.getStatus()!=ViagemStatus.CHEGADA_EM_ATRASO)
						viagem.setStatus(ViagemStatus.CHEGADA_EM_ATRASO);
					viagem.setChegadaReal(now);
				}		
			
			}else if(viagem.getDistanciaPercorrida()>0 && viagem.getSaiuDaCerca()!= null && viagem.getEntrouNaCerca()!=null && viagem.getChegadaReal()!=null && isInCerca(distancia,cerca) && viagem.getStatus()!=ViagemStatus.ENCERRADA){
				viagem.setStatus(ViagemStatus.ENCERRADA);
			}else if(viagem.getDistanciaPercorrida()>0 && viagem.getSaiuDaCerca()!= null && viagem.getEntrouNaCerca()!=null && !isInCerca(distancia,cerca) && viagem.getStatus()!=ViagemStatus.SAIU_DA_CERCA){
				viagem.setStatus(ViagemStatus.SAIU_DA_CERCA);
				viagem.setSaiuDaCerca(now);
				viagem.setEntrouNaCerca(null);
				viagem.setChegadaReal(null);
			}
			
		}
		
		

		private boolean isInCerca(double distancia,Cerca cerca){
			return (distancia) <= cerca.getRaio();
		}
	    

	    
		public void atualizaDistanciaFinalizada(Viagem viagem) {
			List<Object> objs = locationDao.getLocationsFromVeiculo(viagem.getVeiculo(),viagem.getPartida(),viagem.getChegadaReal());
			double distance = getDistance(objs);
			if(distance>0){
				viagem.setDistanciaPercorrida(distance);
			}
		}
		

		public double getDistance(List<Object> objs){
			double distance = 0;
			for (int i = 0; i < objs.size()-1; i++) {
				distance = distance + GeoDistanceCalulator.haverSineDistance(objs.get(i+1),objs.get(i)); 
			}
			return distance;
		}
		
		
		
	}