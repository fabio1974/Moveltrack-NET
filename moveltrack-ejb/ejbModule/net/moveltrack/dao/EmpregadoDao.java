package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.PerfilTipo;

@Stateless
@SuppressWarnings("serial")
public class EmpregadoDao extends DaoBean<Empregado>{


	public EmpregadoDao() { }

	public List<Empregado> findByTerm(String term){
		List<Empregado> list = new ArrayList<Empregado>();
		Query query = getEm().createQuery("select o from Empregado o where UPPER(o.nome) like '%"+term.toUpperCase()+"%' order by o.nome");
		try{
			list = (List<Empregado>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	
	public List<Empregado> findByTipo(PerfilTipo tipo){
		List<Empregado> list = new ArrayList<Empregado>();
		Query query = getEm().createQuery("select o from Empregado o where o.usuario.perfil.tipo=:tipo order by o.nome");
		query.setParameter("tipo",tipo);
		try{
			list = (List<Empregado>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}


	public Empregado findByNome(String nome) {
		try{
		Query query = getEm().createQuery("select o from Empregado o where o.nome =:nome");
		query.setParameter("nome",nome);
		return (Empregado)query.getSingleResult();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	

	
	
	
	public Empregado findByCpf(String cpf){
		Query query = getEm().createQuery("select o from Empregado o where o.cpf=:cpf or o.cpf=:cpf2");
		query.setParameter("cpf",cpf);
		String cpf2 = cpf.replace(".","").replace("-","");
		query.setParameter("cpf2",cpf2);
		try{
			return (Empregado)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	public Empregado findByNomeUsuario(String nomeUsuario) {
		Query query = getEm().createQuery("select o from Empregado o where o.usuario.nomeUsuario=:nomeUsuario");
		query.setParameter("nomeUsuario",nomeUsuario);
		try{
			return (Empregado)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	


}
