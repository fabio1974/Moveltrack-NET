package net.moveltrack.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.poi.ss.formula.functions.T;

import net.moveltrack.domain.Cliente;

@Stateless
public class AlarmesDao extends DaoBean<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getProblemaNaBateria(Cliente cliente) {
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE,-30);
		
		String sql = "select l.dateLocation,l.battery,l.latitude,l.longitude,v.placa,v.marcaModelo from location2 l"+
				" inner join equipamento e on l.imei = e.imei"+
				" inner join veiculo v on v.equipamento_id = e.id"+
				" inner join contrato c on v.contrato_id = c.id"+

				" where 1 = 1 "+
				" and (l.battery = '0' or l.battery = 'LOW')"+
				" and l.dateLocation >:inicio "+
				" and l.dateLocation <:now "+
				" and c.cliente_id =:clienteId";

		
		Query q = getEm().createNativeQuery(sql);
		q.setParameter("inicio",c.getTime());
		q.setParameter("now",new Date());
		q.setParameter("clienteId",cliente.getId());

		try{
			return  (List<Object[]>)q.getResultList();
		}catch(Exception e){
			return null;
		}

	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getExcessoDeVelocidade(Cliente cliente) {
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE,-30);
		
		String sql = "select l.dateLocation,l.velocidade,l.latitude,l.longitude,v.placa,v.marcaModelo from location2 l"+
				" inner join equipamento e on l.imei = e.imei"+
				" inner join veiculo v on v.equipamento_id = e.id"+
				" inner join contrato c on v.contrato_id = c.id"+

				" where 1 = 1 "+
				" and v.velocidadeMaxima > 0"+
				" and l.velocidade > v.velocidadeMaxima "+
				" and l.dateLocation >:inicio "+
				" and l.dateLocation <:now "+
				" and c.cliente_id =:clienteId";

		
		Query q = getEm().createNativeQuery(sql);
		q.setParameter("inicio",c.getTime());
		q.setParameter("now",new Date());
		q.setParameter("clienteId",cliente.getId());

		try{
			return  (List<Object[]>)q.getResultList();
		}catch(Exception e){
			return null;
		}

	}
	

}
