package net.moveltrack.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Location2;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.RelatorioExcessoVelocidade;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.util.GeoDistanceCalulator;
import net.moveltrack.util.Utils;

@Stateless
@SuppressWarnings("serial")
public class LocationDao extends DaoBean<Location>{

	public LocationDao() { }
	
	

	@SuppressWarnings("unchecked")
	public List<Object> getLocationsFromVeiculo(Veiculo veiculo,Date inicio, Date fim){
		
		String orderby = "";
		if(veiculo.getEquipamento().getModelo() == ModeloRastreador.TK103A2 || veiculo.getEquipamento().getModelo() == ModeloRastreador.TK06)
			orderby = " order by l.id ";
		else if(veiculo.getEquipamento().getModelo() == ModeloRastreador.GT06 
				||veiculo.getEquipamento().getModelo() == ModeloRastreador.GT06N
				||veiculo.getEquipamento().getModelo() == ModeloRastreador.CRX1
				||veiculo.getEquipamento().getModelo() == ModeloRastreador.CRX3				
				||veiculo.getEquipamento().getModelo() == ModeloRastreador.CRXN
				||veiculo.getEquipamento().getModelo() == ModeloRastreador.TR02
				||veiculo.getEquipamento().getModelo() == ModeloRastreador.JV200
				||veiculo.getEquipamento().getModelo() == ModeloRastreador.SPOT_TRACE
				)
			orderby = " order by l.dateLocation ";

		boolean isHistorico = Utils.isHistorico(inicio);
		
		String sql = "select l from "+(isHistorico?"Location":"Location2")+" l where l.imei =:imei and "+
				"("+
				"(l.dateLocation >=:inicio and l.dateLocation <=:fim)"+
				"or"+
				"(l.dateLocationInicio >=:inicio and l.dateLocationInicio <=:fim)"+
				"or"+
				"(l.dateLocationInicio <=:inicio and l.dateLocation >=:fim)"+
				")" + orderby;					
		
		Query q = getEm().createQuery(sql);
		q.setParameter("imei",veiculo.getEquipamento().getImei());
		q.setParameter("inicio",inicio);
		q.setParameter("fim",fim);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		System.out.println(sdf.format(fim));
		
		List<Object> result = q.getResultList(); 
		
		return result;
	}

	
/*	@SuppressWarnings("unchecked")
	public List<MapaEfetivo> getLastDayLastLocations() {
		List<MapaEfetivo> list = null;
		String sql =	"select loc1.imei, loc1.dateLocation, loc1.latitude, loc1.longitude, loc1.velocidade, vi.id, v.placa, vi.
from location loc1 
inner join equipamento equ on equ.imei = loc1.imei
inner join veiculo v on v.equipamento_id = equ.id
inner join viagem vi on vi.veiculo_id = v.id
inner join  (select imei,max(dateLocation) as ultimaDateLocation from location group by 1) loc2
      on ( loc1.imei = loc2.imei and loc2.ultimaDateLocation = loc1.dateLocation)
      
where vi.status <> 'FINALIZADA'
      
      group by loc1.imei,loc1.latitude,loc1.longitude, loc1.velocidade, vi.id, v.placa
      order by loc1.dateLocation desc;";


		Query q = getEm().createNativeQuery(sql,MapaEfetivo.class);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,7);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		q.setParameter("dataP",c.getTime());
		
		list =  (List<MapaEfetivo>) q.getResultList();
		return list;

	}
*/


	@SuppressWarnings("unchecked")
	public Location getLastLocationFromVeiculo(Veiculo veiculo) {
		String sql = "select l from Location l where l.imei = '"+veiculo.getEquipamento().getImei()+"' and l.dateLocation<=:now order by l.dateLocation desc ";
		Query q = getEm().createQuery(sql);
		q.setParameter("now",new Date());
		q.setMaxResults(1);
		try{
			return  (Location)q.getResultList().get(0);
		}catch(Exception e){
			return null;
		}
	}
	
	
	public Location getPreviousLocation(Location current) {
		String sql = "select l from Location l where l.imei = '"+current.getImei()+"' and l.dateLocation<:currentDate order by l.dateLocation desc ";
		Query q = getEm().createQuery(sql);
		q.setParameter("currentDate",current.getDateLocation());
		q.setMaxResults(1);
		try{
			return  (Location)q.getResultList().get(0);
		}catch(Exception e){
			return null;
		}
	}
	


	
	
	@SuppressWarnings("unchecked")
	public Location2 getLastLocation2FromVeiculo(Veiculo veiculo) {
		try{
			String sql = "select l from Location2 l where l.imei = '"+veiculo.getEquipamento().getImei()+"' and l.dateLocation<=:now order by l.dateLocation desc ";
			Query q = getEm().createQuery(sql);
			q.setParameter("now",new Date());
			q.setMaxResults(1);
			return  (Location2)q.getResultList().get(0);
		}catch(Exception e){
			return null;
		}
	}

	
	





	@SuppressWarnings("unchecked")
	public List<Location> getLastLocations(Integer clienteId) {
		
		List<Location> list = new ArrayList<Location>();
		
		String sql = "select substring(l2.imei,10) as id, l2.imei,l2.dateLocation, l2.velocidade, l2.latitude, l2.longitude,"
				+" l2.alarm,l2.alarmType,l2.battery,l2.bloqueio,l2.comando,l2.dateLocationInicio,l2.gps,"
				+" l2.gsm,l2.ignition,l2.mcc,0 as satelites,l2.sos,l2.volt,l2.version"

				+" from location l2"
				+" inner join (select l.imei as imei, max(l.dateLocation) as dateLocation from location2 l group by l.imei)  loc2"
				+" on loc2.imei = l2.imei and loc2.dateLocation = l2.dateLocation"

				+" inner join equipamento e on e.imei = l2.imei"
				+" inner join veiculo v on v.equipamento_id = e.id"
				+" inner join contrato c on v.contrato_id = c.id"
				+" inner join pessoa p on c.cliente_id = p.id"

				+" where p.id =:clienteId and v.status='ATIVO'"

				+" group by l2.imei,l2.dateLocation, l2.velocidade, l2.latitude, l2.longitude," 
				+" l2.alarm,l2.alarmType,l2.battery,l2.bloqueio,l2.comando,l2.dateLocationInicio,l2.gps,"
				+" l2.gsm,l2.ignition,l2.mcc,l2.sos,l2.volt,l2.version,"
				+" id order by l2.dateLocation desc";

		
		Query q = getEm().createNativeQuery(sql,Location.class);
		q.setParameter("clienteId",clienteId);
		list = q.getResultList();
		return list;
	}
	
	public int deleteUntilDateFromLocation2(Date date) {
		String sql = "delete from Location2 l where l.dateLocation <:dateLocation";
		Query q = getEm().createQuery(sql);
		q.setParameter("dateLocation",date);
		return q.executeUpdate();
	}
	
	
	public int deleteUntilDateFromLocation(Date date) {
		String sql = "delete from Location l where l.dateLocation <:dateLocation";
		Query q = getEm().createQuery(sql);
		q.setParameter("dateLocation",date);
		return q.executeUpdate();
	}

	
	



	public long countByImei(String imei) {
		try {
			Query q = getEm().createQuery("select count(o) from Location o where o.imei=:imei");
			q.setParameter("imei",imei);
			return  (Long) q.getSingleResult();
		} catch (Exception e) {
			return 0;
		}
	}


	public Date getLastUpdate() {
		String sql = "SELECT update_time FROM   information_schema.tables"
				+ "	WHERE  table_schema = 'moveltrack'"
				+ "	       AND table_name = 'location2'";
		try {
			Query q = getEm().createNativeQuery(sql);
			return  (Date) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}



	public double getGSSpeed(Location current) {
		Location previous = getPreviousLocation(current);
		if(previous == null)
			return 0;
		double ds = GeoDistanceCalulator.vicentDistance(previous,current);
		double dt = current.getDateLocation().getTime() - previous.getDateLocation().getTime();
		return (ds/1000)/(dt/3600000);
	}



	@SuppressWarnings("unchecked")
	public List<RelatorioExcessoVelocidade> getExcessoVelocidadeList(Cliente cliente, Integer velocidade, Date inicio, Date fim) {

		List<RelatorioExcessoVelocidade> list = new ArrayList<RelatorioExcessoVelocidade>();
		
		String sql = " select l.id, '' as endereco, l.dateLocation, v.placa,v.marcaModelo, l.latitude, l.longitude, l.velocidade " 
				 +" from location l"
				 +" inner join equipamento e on e.imei = l.imei"
				 +" inner join veiculo v on v.equipamento_id = e.id"
				 +" inner join contrato c on v.contrato_id = c.id"
				 +" inner join pessoa p on c.cliente_id = p.id"

				 +" where p.id =:clienteId and v.status='ATIVO'"
				 
				 +" and l.dateLocation >=:inicio"
				 +" and l.dateLocation <=:fim"
				 +" and l.velocidade >:velocidade"

				 +" order by v.placa,l.dateLocation"; 

		
		Query q = getEm().createNativeQuery(sql,RelatorioExcessoVelocidade.class);
		q.setParameter("clienteId",cliente.getId());
		q.setParameter("inicio",inicio);
		q.setParameter("fim",fim);
		q.setParameter("velocidade",velocidade);
		
		list = (List<RelatorioExcessoVelocidade>)q.getResultList();
		return list;
	}
	

	
	
	


}

