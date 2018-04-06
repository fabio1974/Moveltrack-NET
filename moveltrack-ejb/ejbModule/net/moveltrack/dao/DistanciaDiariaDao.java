package net.moveltrack.dao;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Chip;
import net.moveltrack.domain.DistanciaDiaria;
import net.moveltrack.domain.Veiculo;

@Stateless
@SuppressWarnings("serial")
public class DistanciaDiariaDao extends DaoBean<DistanciaDiaria>{

	public DistanciaDiariaDao() { }

	public DistanciaDiaria findByVeiculoAndDataComputada(Veiculo veiculo, Date dataComputada) {
		Query query = getEm().createQuery("select o from DistanciaDiaria o where o.veiculo.id =:veiculoId and o.dataComputada=:dataComputada");
		query.setParameter("veiculoId",veiculo.getId());
		query.setParameter("dataComputada",dataComputada);
		try{
			return (DistanciaDiaria)query.getSingleResult();	
		}catch(Exception e){
			return null;
		}
	}
	
	

}
