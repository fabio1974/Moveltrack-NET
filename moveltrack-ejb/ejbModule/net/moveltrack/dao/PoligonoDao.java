package net.moveltrack.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import net.moveltrack.domain.Poligono;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Vertice;

@Stateless
@SuppressWarnings("serial")
public class PoligonoDao extends DaoBean<Poligono>{

	@Inject VerticeDao verticeDao;

	@Transactional
	public void removePoligonoByVeiculo(Veiculo veiculo) {
		Poligono poligono = findByVeiculo(veiculo);
		if(poligono!=null && poligono.getVertices()!=null){
			for (Vertice vertice : poligono.getVertices()) {
				verticeDao.remover(vertice);
			}
			remover(poligono);
		}
	}

	

	public Poligono findByVeiculo(Veiculo veiculo) {
		Query query = getEm().createQuery("select o from Poligono o where o.veiculo.id=:veiculoId");
		query.setParameter("veiculoId",veiculo.getId());
		try{
			Poligono poligono = (Poligono)query.getSingleResult();
			poligono.setVertices(verticeDao.findByPoligono(poligono));
			return poligono;
		}catch(Exception e){
			return null;
		}
	}



	@Transactional
	public void salvaPoligono(Poligono poligono) {
		for (Vertice vertice : poligono.getVertices()) {
			vertice.setPoligono(poligono);
			verticeDao.salvar(vertice);
		}
		salvar(poligono);
	}



	public List<Poligono> findAtivos(){
		try{
			Query query = getEm().createQuery("select o from Poligono o where o.ativo=:ativo and o.veiculo.contrato.cliente.clienteConfig.alarmeCerca=:alarmeCerca");
			query.setParameter("ativo",true);
			query.setParameter("alarmeCerca",true);
			return (List<Poligono>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	
}
