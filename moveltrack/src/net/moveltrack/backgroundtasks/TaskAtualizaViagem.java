package net.moveltrack.backgroundtasks;

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


@ApplicationScoped
public class TaskAtualizaViagem extends Task {
	
	    @Inject
	    ViagemThreadDao viagemThreadDao;
	    
		@Inject
		LocationDao locationDao;
	    
		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskAtualizaViagem() {
			
		}
	    

	    @Override
	    public void run() {
			try{
				List<Viagem> viagensAtivas = viagemThreadDao.findNaoEncerradas();
				System.out.println("------------------------------------------------------------------------");
				System.out.println(" atualizando lote de viagens ativas: " +viagensAtivas.size()+" viagens!");
				System.out.println("------------------------------------------------------------------------");
				for (Viagem viagem : viagensAtivas) {
					//System.out.println(viagem.getPartida());
					atualizaDistancia(viagem);
					atualizaStatus(viagem);
					viagemThreadDao.merge(viagem);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
	    }

	    
		public void atualizaDistancia(Viagem viagem) {
			List<Object> objs = locationDao.getLocationsFromVeiculo(viagem.getVeiculo(),viagem.getPartida(),new Date());
			double distance = getDistance(objs);
			if(distance>0){
				viagem.setDistanciaPercorrida(distance);
			}
		}
		
		
		public void atualizaDistanciaFinalizada(Viagem viagem) {
			List<Object> objs = locationDao.getLocationsFromVeiculo(viagem.getVeiculo(),viagem.getPartida(),viagem.getChegadaReal());
			double distance = getDistance(objs);
			if(distance>0){
				viagem.setDistanciaPercorrida(distance);
			}
		}
		
		
		
		private void atualizaStatus(Viagem viagem) {
			
			Date now = new Date();
			Location lastLoc = locationDao.getLastLocationFromVeiculo(viagem.getVeiculo());
			Cerca cerca = viagem.getCliente().getCerca();
			if(cerca==null)
				return;
			
			double distancia = GeoDistanceCalulator.haverSineDistance(cerca.getLat1(),cerca.getLon1(),lastLoc.getLatitude(),lastLoc.getLongitude());

			System.out.println("----- atualizando viagem:  " + viagem.getId()+ "------");
			
			if(viagem.getDistanciaPercorrida()==0 && viagem.getSaiuDaCerca()==null && isInCerca(distancia,cerca)){
				if(now.getTime() - viagem.getPartida().getTime() > 15*60*1000)  //15 minutos de tolerancia
					viagem.setStatus(ViagemStatus.PARTIDA_EM_ATRASO);
			}else if(viagem.getDistanciaPercorrida()>0 && viagem.getSaiuDaCerca()==null && isInCerca(distancia,cerca)){
				viagem.setStatus(ViagemStatus.SAINDO);
			}else if(viagem.getDistanciaPercorrida()>0 && viagem.getSaiuDaCerca()==null && !isInCerca(distancia,cerca)){
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

		
		public double getDistance(List<Object> objs){
			double distance = 0;
			//System.out.println("somando "+ objs.size() + " intervalos  ");
			for (int i = 0; i < objs.size()-1; i++) {
				distance = distance + GeoDistanceCalulator.haverSineDistance(objs.get(i+1),objs.get(i)); 
			}
			return distance;
		}
	}