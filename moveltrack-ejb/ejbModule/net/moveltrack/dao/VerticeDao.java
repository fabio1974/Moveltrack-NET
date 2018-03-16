package net.moveltrack.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Poligono;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Vertice;

@Stateless
@SuppressWarnings("serial")
public class VerticeDao extends DaoBean<Vertice>{

	public List<Vertice> findByPoligono(Poligono poligono) {
		Query query = getEm().createQuery("select o from Vertice o where o.poligono.id=:poligonoId");
		query.setParameter("poligonoId",poligono.getId());
		try {
			return (List<Vertice>)query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Vertice> findByVeiculo(Veiculo veiculo) {
		Query query = getEm().createQuery("select o from Vertice o where o.poligono.veiculo.id=:veiculoId");
		query.setParameter("veiculoId",veiculo.getId());
		try {
			return (List<Vertice>)query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

}
