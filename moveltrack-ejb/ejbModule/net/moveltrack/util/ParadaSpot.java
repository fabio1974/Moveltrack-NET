package net.moveltrack.util;

import java.util.ArrayList;
import java.util.List;

import net.moveltrack.domain.Location;

class ParadaSpot{
		List<Location> pontosDeParada;
		
		public ParadaSpot() {
			pontosDeParada = new ArrayList<Location>(); 
		}
		
		public void abreParada(Location inicio) {
			pontosDeParada.add(inicio);
		}
		
		public void mantemParada(Location meio) {
			pontosDeParada.add(meio);
		}
		
		public boolean isAberta() {
			return !pontosDeParada.isEmpty();
		}
		
		public void fechaParada(List<Location> pontosMapa, Location fim) {
			pontosDeParada.add(fim);
			Location parada = getAverageLocation(pontosDeParada);
			pontosMapa.add(parada);
			pontosDeParada.clear();
		}
		
		public void fechaEAbreNova(List<Location> pontosMapa, Location inicio) {
			Location parada = getAverageLocation(pontosDeParada);
			pontosMapa.add(parada);
			pontosDeParada.clear();
			abreParada(inicio);
		}
		
		public Location getAverageLocation(List<Location> parada){	
			//----------------------------------------------------------------------------------------------------------
			//O código comentado abaixo faz a média dos pontos. Esse algoritmo porém não é recomendável em casos de roubo ou furto.
			//----------------------------------------------------------------------------------------------------------
				if(parada.size()<=0)
					return null;

				int count = 0;
				double latMed=0,lonMed=0;
				for(Location location: parada){
					latMed+=location.getLatitude();
					lonMed+=location.getLongitude();

					String ignition= location.getIgnition();
					if(ignition!=null && ignition.equals("1"))
						count++;
				}
				latMed=latMed/parada.size();
				lonMed=lonMed/parada.size();
				
				Location firstLoc =  parada.get(0);
				Location lastLoc =  parada.get(parada.size()-1);
				Location loc = new Location();

				loc.setIgnition(count>parada.size()/2?"1":"0");
				loc.setLatitude(latMed);
				loc.setLongitude(lonMed);
				loc.setVelocidade(0);
				loc.setDateLocationInicio(parada.get(0).getDateLocationInicio());
				loc.setDateLocation(parada.get(parada.size()-1).getDateLocation());
				loc.setAlarm(lastLoc.getAlarm());
				loc.setAlarmType(lastLoc.getAlarmType());
				loc.setBattery(lastLoc.getBattery());
				loc.setBloqueio(lastLoc.getBloqueio());
				loc.setComando("STOP");
				loc.setGps(lastLoc.getGps());
				loc.setGsm(lastLoc.getGsm());
				loc.setId(lastLoc.getId());
				loc.setImei(lastLoc.getImei());
				loc.setMcc(lastLoc.getMcc());
				loc.setSatelites(lastLoc.getSatelites());
				loc.setSos(lastLoc.getSos());
				//loc.setVersion(lastLoc.getVersion());
				loc.setVolt(lastLoc.getVolt());
				return loc;
			}
		
	}