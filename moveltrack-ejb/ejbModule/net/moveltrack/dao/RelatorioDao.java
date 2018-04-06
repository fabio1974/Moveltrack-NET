package net.moveltrack.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Veiculo;

@SuppressWarnings("serial")
@Stateless
public class RelatorioDao implements Serializable {

	
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager em;
 
    @SuppressWarnings("unchecked")
	public List<Object[]> getRelatorioDistanciaPercorrida(Cliente cliente, Veiculo veiculo,	Date inicio, Date fim, String orderby) {
		List<Object[]> list = new ArrayList<Object[]>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		
		String sql = " select v.placa,v.marcaModelo, sum(d.distanciaPercorrida) as distancia from distanciadiaria d" + 
				" inner join veiculo v on v.id = d.veiculo_id" + 
				" inner join contrato c on v.contrato_id = c.id" + 
				" inner join pessoa p on c.cliente_id = p.id" + 
				" where p.id =" + cliente.getId() +
				" and v.status='ATIVO'" +
				(veiculo!=null?" and v.id="+veiculo.getId():" ") +
				" and d.dataComputada >='"+ sdf.format(inicio)+"'"+
				" and d.dataComputada <='"+ sdf.format(fim)+"'"+
				" group by v.placa,v.marcaModelo" + 
				(orderby.equals("distancia")?" order by distancia desc ":(orderby.equals("marcaModelo")?" order by v.marcaModelo ":" order by v.placa "));

		System.out.println(sql);
		
		Query q = em.createNativeQuery(sql);

		
		
		list = (List<Object[]>)q.getResultList();
		return list;	
	}



}
