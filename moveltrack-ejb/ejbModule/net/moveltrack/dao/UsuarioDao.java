package net.moveltrack.dao;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;

import net.moveltrack.domain.Usuario;

@Stateless
@SuppressWarnings("serial")
public class UsuarioDao extends DaoBean<Usuario>{


	public UsuarioDao() { }

	public Usuario findByNomeUsuario(String nomeUsuario) {
		Query query = getEm().createQuery("select o from Usuario o where o.nomeUsuario=:nomeUsuario");
		query.setParameter("nomeUsuario",nomeUsuario);
		try{
			return (Usuario)query.getSingleResult();
		}catch(Exception e){
			//System.err.println(e.getMessage());
			return null;
		}
	}
	
	
	
	

}
