package net.moveltrack.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import net.moveltrack.domain.Marcador;
import net.moveltrack.domain.Poligono;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Vertice;

@Stateless
@SuppressWarnings("serial")
public class MarcadorDao extends DaoBean<Marcador>{

	@Inject VerticeDao verticeDao;

	@Transactional
	public void removeMarcadoresByVeiculo(Veiculo veiculo) {
		List<Marcador> list = findByVeiculo(veiculo);
		if (list != null) {
			for (Marcador marcador : list) {
				remover(marcador);
			}
		}
	}

	

	@SuppressWarnings("unchecked")
	public List<Marcador> findByVeiculo(Veiculo veiculo) {
		List<Marcador> list = null;
		Query query = getEm().createQuery("select o from Marcador o where o.veiculo.id=:veiculoId");
		query.setParameter("veiculoId",veiculo.getId());
		try{
			list = (List<Marcador>)query.getResultList();
			return list;
		}catch(Exception e){
			return null;
		}
	}



	@Transactional
	public void salvaMarcadores(List<Marcador> list,Veiculo veiculo) {
		for (Marcador marcador : list) {
			marcador.setVeiculo(veiculo);
			salvar(marcador);
		}
	}
	
}
