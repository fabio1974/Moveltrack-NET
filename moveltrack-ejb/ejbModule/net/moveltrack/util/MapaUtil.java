package net.moveltrack.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.moveltrack.domain.Location;
import net.moveltrack.domain.Location2;
import net.moveltrack.domain.Mapa;
import net.moveltrack.domain.Vertice;


public class MapaUtil  {

	
	public  static List<Location> otimizaPontosDoBanco(List<Object> pontosDoBanco, Date inicio, Date fim) {
		
		List<Location> pontosMapa = new ArrayList<Location>();
		
		if(pontosDoBanco==null || pontosDoBanco.size()==0)
			return pontosMapa;
		
		if(pontosDoBanco.size()==1){
			pontosMapa.add(getLocationFromObject(pontosDoBanco.get(0)));
			//center.setLocation(pontosMapa.get(0).getLongitude(),pontosMapa.get(0).getLatitude());
		}else {
			List<Location> pontosDeParada = new ArrayList<Location>();
			
			Location initLoc = getLocationFromObject(pontosDoBanco.get(0));
			initLoc.setDateLocation(inicio);
			initLoc.setDateLocationInicio(inicio);
			pontosDoBanco.add(0,initLoc);
			
			
			
			Date now = new Date();
			
			Location fimLoc = getLocationFromObject(pontosDoBanco.get(pontosDoBanco.size()-1));
			if(fimLoc.getVelocidade()<=0) {
				fimLoc.setDateLocation(fim.before(now)?fim:now);
				fimLoc.setDateLocationInicio(fim.before(now)?fim:now);
				pontosDoBanco.add(fimLoc);				
			}
			
			
			int size = pontosDoBanco.size();


			Location lastLoc=null;
			for (int i = 0; i < size; i++) {
				   Location loc = getLocationFromObject(pontosDoBanco.get(i));	

				   if(lastLoc!=null && lastLoc.getDateLocation().equals(loc.getDateLocation()) && lastLoc.getDateLocationInicio().equals(loc.getDateLocationInicio())){
					   lastLoc = loc;
					   continue;
				   }
					   
				   
				   if(i==0 && loc.getDateLocationInicio().before(inicio))
							loc.setDateLocationInicio(inicio);
				   if((i==size-1) && loc.getDateLocation().after(fim))
							loc.setDateLocation(fim);

				   boolean isFar = isFarOfLastLocation(loc,lastLoc);
				   if(loc.getVelocidade()>0 || isFar){
			            if(pontosDeParada.size()>0){
			            	Location parada = getAverageLocation(pontosDeParada);
			            	pontosDeParada.clear();
			            	if(isFar)
			            		pontosDeParada.add(loc);
			            	pontosMapa.add(parada);
			            }
			            pontosMapa.add(loc);
				   }else{
					   pontosDeParada.add(loc);
				   }
				   lastLoc = loc;
			}
			//coloca a última parada
		    if(pontosDeParada.size()>0){
		    	for (Location location : pontosDeParada) {
		    		pontosMapa.add(location);
				}
		       	Location parada = getLastStop(pontosDeParada);
	       		//paradas.add(parada);
	       		pontosMapa.add(parada);
		    }
		}
		return pontosMapa;
	    //coloca o centro no veículo, ultima posição
		//if(pontosMapa.size()>0)//{
			//Location loc = pontosMapa.get(pontosMapa.size()-1);
			//center.setLocation(loc.getLongitude(),loc.getLatitude());
		//}else
			//center.setLocation(-51.82,-13.34);
	}
	
	

	public static boolean isParada(Location loc){
		if(loc.getVelocidade()>0)
			return false;
		return loc.getDateLocation().getTime() - loc.getDateLocationInicio().getTime() > 3*60*1000;
	}	
	
	
	private static boolean isFarOfLastLocation(Location loc, Location lastLoc) {
		if(lastLoc == null || lastLoc.getVelocidade()>0 || loc.getVelocidade()>0)// TODO Auto-generated method stub
			return false;
		//System.out.println(VicentDistance.distVincenty(loc.getLatitude(),loc.getLongitude(),lastLoc.getLatitude(),lastLoc.getLongitude()));
		return GeoDistanceCalulator.vicentDistance(loc.getLatitude(),loc.getLongitude(),lastLoc.getLatitude(),lastLoc.getLongitude())>100;
	}



	public static MapFeatures getFeatures(List<Location> amostra) {
		MapFeatures mp = new MapFeatures();
		if(amostra.size()==0){
			mp.center.setLocation(-51.82,-13.34);
			mp.pNE = mp.center;
			mp.pSW = mp.center;
			mp.zoom = 8;
		}else if(amostra.size()==1){
			mp.center.setLocation(amostra.get(0).getLongitude(),amostra.get(0).getLatitude());
			mp.pNE = mp.center;
			mp.pSW = mp.center;
			mp.zoom = 21;
		}else {
			double maxLon= -181;
			double maxLat= -91;
			double minLon= 181;
			double minLat= 91;

			for (int i = 0; i < amostra.size(); i++) {
				Location loc = amostra.get(i);	
				if(maxLon < loc.getLongitude())
					maxLon = loc.getLongitude();
				if(maxLat < loc.getLatitude())
					maxLat = loc.getLatitude();
				if(minLon > loc.getLongitude())
					minLon = loc.getLongitude();
				if(minLat > loc.getLatitude())
					minLat = loc.getLatitude();
			}
			mp.pNE.setLocation(maxLon,maxLat);
			mp.pSW.setLocation(minLon,minLat);
			mp.center.setLocation((minLon+maxLon)/2,(minLat+maxLat)/2);
			mp.zoom = getBestZoom(mp.pNE, mp.pSW);
		}
		return mp;
	}	
	
	
	public static MapFeatures getPoligonFeatures(List<Vertice> vertices) {
		MapFeatures mp = new MapFeatures();
		if(vertices.size()==0){
			mp.center.setLocation(-51.82,-13.34);
			mp.pNE = mp.center;
			mp.pSW = mp.center;
			mp.zoom = 8;
		}else if(vertices.size()==1){
			mp.center.setLocation(vertices.get(0).getLng(),vertices.get(0).getLat());
			mp.pNE = mp.center;
			mp.pSW = mp.center;
			mp.zoom = 21;
		}else {
			double maxLon= -181;
			double maxLat= -91;
			double minLon= 181;
			double minLat= 91;

			for (int i = 0; i < vertices.size(); i++) {
				Vertice vertice = vertices.get(i);	
				if(maxLon < vertice.getLng())
					maxLon = vertice.getLng();
				if(maxLat < vertice.getLat())
					maxLat = vertice.getLat();
				if(minLon > vertice.getLng())
					minLon = vertice.getLng();
				if(minLat > vertice.getLat())
					minLat = vertice.getLat();
			}
			mp.pNE.setLocation(maxLon,maxLat);
			mp.pSW.setLocation(minLon,minLat);
			mp.center.setLocation((minLon+maxLon)/2,(minLat+maxLat)/2);
			int cauculado = getBestZoom(mp.pNE, mp.pSW);
			mp.zoom = cauculado > 1? cauculado-1:cauculado;
			
		}
		return mp;
	}	
	
	
	public static Mapa getMapa(List<Location> amostra) {
		Mapa mapa = new Mapa();

		
		Location centro = new Location();
		mapa.setCentro(centro);
		
		if(amostra.size()==0){
			mapa.getCentro().setLongitude(-51.82);
			mapa.getCentro().setLatitude(-13.34);
			mapa.setpNE(mapa.getCentro());
			mapa.setpSW(mapa.getCentro());
			mapa.setZoom(8);
		}else if(amostra.size()==1){
			mapa.getCentro().setLatitude(amostra.get(0).getLatitude());
			mapa.getCentro().setLongitude(amostra.get(0).getLongitude());
			mapa.setpNE(mapa.getCentro());
			mapa.setpSW(mapa.getCentro());
			mapa.setZoom(21);		
		}else {
		
			double maxLon= -181;
			double maxLat= -91;
			double minLon= 181;
			double minLat= 91;

			for (int i = 0; i < amostra.size(); i++) {
				Location loc = amostra.get(i);	
				if(maxLon < loc.getLongitude())
					maxLon = loc.getLongitude();
				if(maxLat < loc.getLatitude())
					maxLat = loc.getLatitude();
				if(minLon > loc.getLongitude())
					minLon = loc.getLongitude();
				if(minLat > loc.getLatitude())
					minLat = loc.getLatitude();
			}
			
			Location ne = new Location();
			ne.setLatitude(maxLat);
			ne.setLongitude(maxLon);
			mapa.setpNE(ne);
			
			Location sw = new Location();
			sw.setLatitude(minLat);
			sw.setLongitude(minLon);
			mapa.setpSW(sw);

			mapa.getCentro().setLatitude((minLat+maxLat)/2);
			mapa.getCentro().setLongitude((minLon+maxLon)/2);

			mapa.setZoom(getBestZoom(mapa.getpNE(), mapa.getpSW()));
		}
		return mapa;
	}	
	
	
	
	public static int getBestZoom(Location pNE,Location pSW){
		int GLOBE_WIDTH = 256;
		double angleX = pNE.getLongitude() - pSW.getLongitude();
		double angleY = pNE.getLatitude() - pSW.getLatitude();
		int delta = 0;
		if(angleY > angleX) {
		    angleX = angleY;
		    delta = 1;
		}
		if (angleX < 0) {
		    angleX += 360;
		}
		double N = Math.log(960 * 360 / angleX / GLOBE_WIDTH);
		double r = Math.floor(N/Math.log(2));
		int zoom = (int)r - delta;
		if(zoom>21)
			zoom = 21;
		return  zoom;	
	}
	
	
	
	

	/*public boolean isIn(Double mapNE, Double mapSW) {
		return pNE.x <= mapNE.x && pNE.y<= mapNE.y && pSW.x >= mapSW.x && pSW.y >= mapSW.y;
	}*/
	
	public static int getBestZoom(Point2D.Double pNE,Point2D.Double pSW){
		int GLOBE_WIDTH = 256;
		double angleX = pNE.x - pSW.x;
		double angleY = pNE.y - pSW.y;
		int delta = 0;
		if(angleY > angleX) {
		    angleX = angleY;
		    delta = 1;
		}
		if (angleX < 0) {
		    angleX += 360;
		}
		double N = Math.log(960 * 360 / angleX / GLOBE_WIDTH);
		double r = Math.floor(N/Math.log(2));
		int zoom = (int)r - delta;
		if(zoom>21)
			zoom = 21;
		return  zoom;	
	}
	
	
	public static int getBestZoom(float radius){
		return (int) Math.round(14-Math.log(radius)/Math.log(2));	
	}
	
	

	
	
	public static Location getLocationFromObject(Object obj){
		if(obj instanceof Location)
			return (Location)obj;
		else
			return location2ToLocation(obj);
	}
	
	private static Location location2ToLocation(Object obj) {
		if(obj==null)
			return null;
		Location loc = new Location();
		loc.setIgnition(((Location2)obj).getIgnition());
		loc.setLatitude(((Location2)obj).getLatitude());
		loc.setLongitude(((Location2)obj).getLongitude());
		loc.setVelocidade(((Location2)obj).getVelocidade());
		loc.setDateLocationInicio(((Location2)obj).getDateLocationInicio());
		loc.setDateLocation(((Location2)obj).getDateLocation());
		loc.setAlarm(((Location2)obj).getAlarm());
		loc.setAlarmType(((Location2)obj).getAlarmType());
		loc.setBattery(((Location2)obj).getBattery());
		loc.setBloqueio(((Location2)obj).getBloqueio());
		loc.setComando(((Location2)obj).getComando());
		loc.setGps(((Location2)obj).getGps());
		loc.setGsm(((Location2)obj).getGsm());
		loc.setId(((Location2)obj).getId());
		loc.setImei(((Location2)obj).getImei());
		loc.setMcc(((Location2)obj).getMcc());
		loc.setSatelites(((Location2)obj).getSatelites());
		loc.setSos(((Location2)obj).getSos());
		//loc.setVersion(((Location2)obj).getVersion());
		loc.setVolt(((Location2)obj).getVolt());
		return loc;
	}



	public static Location getLastStop(List<Location> parada){
		
		if(parada.size()<=0)
			return null;

		Location firstLoc =  parada.get(0);
		Location lastLoc =  parada.get(parada.size()-1);
		Location loc = new Location();

		loc.setIgnition(lastLoc.getIgnition());
		loc.setLatitude(lastLoc.getLatitude());
		loc.setLongitude(lastLoc.getLongitude());
		loc.setVelocidade(0);
		loc.setDateLocationInicio(firstLoc.getDateLocationInicio());
		loc.setDateLocation(lastLoc.getDateLocation());
		loc.setAlarm(lastLoc.getAlarm());
		loc.setAlarmType(lastLoc.getAlarmType());
		loc.setBattery(lastLoc.getBattery());
		loc.setBloqueio(lastLoc.getBloqueio());
		loc.setComando(lastLoc.getComando());
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
	
	public static Location getAverageLocation(List<Location> parada){	
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
		loc.setComando(lastLoc.getComando());
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

/*	public String getTipo() {
		if(tipo == null){
			switch (veiculo.getTipo()) {
			case automovel:
				tipo = "carro";
				break;
			case pickup:
				tipo = "pickup";
				break;
			case caminhao:
				tipo = "truck";
				break;
			case trator:
				tipo = "trator";
				break;
			default:
				tipo = "";
				break;
			}
		}
		return tipo;
	}*/
	
	
}
