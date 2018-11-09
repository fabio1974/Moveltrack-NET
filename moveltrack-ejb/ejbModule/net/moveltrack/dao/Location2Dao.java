package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Location2;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.util.GeoDistanceCalulator;

@Stateless
@SuppressWarnings("serial")
public class Location2Dao extends DaoBean<Location2>{

	public Location2Dao() { }
	

	@SuppressWarnings("unchecked")
	public Location2 getLastLocation2FromImei(String imei) {
		String sql = "select l from Location2 l where l.imei = '"+imei+"'  order by l.dateLocation desc ";
		Query q = getEm().createQuery(sql);
		q.setMaxResults(1);
		try{
			return  (Location2)q.getResultList().get(0);
		}catch(Exception e){
			return null;
		}
	}



	public boolean existsWithThaDateAndImei(Date dateLocation, String imei) {
		Location2 l2 = null;
		String sql = "select l from Location2 l where l.imei =:imei and l.dateLocation =:dateLocation   ";
		Query q = getEm().createQuery(sql);
		q.setParameter("imei",imei);
		q.setParameter("dateLocation",dateLocation);
		q.setMaxResults(1);
		try{
			l2 =  (Location2)q.getResultList().get(0);
			return l2!=null;
		}catch(Exception e){
			return false;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Location2> getLocations2FromEquipamento(Equipamento equipamento,Date inicio, Date fim){
		
		if(equipamento==null)
			return new ArrayList<Location2>();
		
		String orderby = "";
		if(equipamento.getModelo() == ModeloRastreador.TK103A2 || equipamento.getModelo() == ModeloRastreador.TK06)
			orderby = " order by l.id ";
		else if(equipamento.getModelo() == ModeloRastreador.GT06 
				||equipamento.getModelo() == ModeloRastreador.GT06N
				||equipamento.getModelo() == ModeloRastreador.CRX1
				||equipamento.getModelo() == ModeloRastreador.CRX3				
				||equipamento.getModelo() == ModeloRastreador.CRXN
				||equipamento.getModelo() == ModeloRastreador.TR02
				||equipamento.getModelo() == ModeloRastreador.JV200
				||equipamento.getModelo() == ModeloRastreador.SPOT_TRACE
				)
			orderby = " order by l.dateLocation ";


		
		String sql = "select l from Location2 l where l.imei =:imei and "+
				"("+
				"(l.dateLocation >=:inicio and l.dateLocation <=:fim)"+
				"or"+
				"(l.dateLocationInicio >=:inicio and l.dateLocationInicio <=:fim)"+
				"or"+
				"(l.dateLocationInicio <=:inicio and l.dateLocation >=:fim)"+
				")" + orderby;					
		
		
		
		Query q = getEm().createQuery(sql);
		q.setParameter("imei",equipamento.getImei());
		q.setParameter("inicio",inicio);
		q.setParameter("fim",fim);
		
		
		List<Location2> result = q.getResultList(); 
		
		return result;
	}

	
	public double getGSSpeed(Location2 current) {
		Location2 previous = getPreviousLocation2(current);
		if(previous == null)
			return 0;
		double ds = GeoDistanceCalulator.vicentDistance(previous,current);
		double dt = current.getDateLocation().getTime() - previous.getDateLocation().getTime();
		double speed = (ds/1000)/(dt/3600000);
		//speed = speed<3?0:speed; 
		return speed;
	}
	
	public Location2 getPreviousLocation2(Location2 current) {
		String sql = "select l from Location2 l where l.imei = '"+current.getImei()+"' and l.dateLocation<:currentDate order by l.dateLocation desc ";
		Query q = getEm().createQuery(sql);
		q.setParameter("currentDate",current.getDateLocation());
		q.setMaxResults(1);
		try{
			return  (Location2)q.getResultList().get(0);
		}catch(Exception e){
			return null;
		}
	}




}

