package net.moveltrack.dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Firebase;
import net.moveltrack.domain.Usuario;

@Stateless
@SuppressWarnings("serial")
public class FirebaseDao extends DaoBean<Firebase>{

	public FirebaseDao() { }


	public Firebase findBySerialAndUsuario(Firebase firebase) {
			Query query = getEm().createQuery("select o from Firebase o where o.serial=:serial and o.usuario.id =: usuarioId");
			query.setParameter("serial",firebase.getSerial());
			query.setParameter("usuarioId",firebase.getUsuario().getId());
			try{
				return (Firebase)query.getSingleResult();
			}catch(Exception e){
				//e.printStackTrace();
				return null;
			}
	}
	
	
	public Firebase findBySerial(Firebase firebase) {
		Query query = getEm().createQuery("select o from Firebase o where o.serial=:serial");
		query.setParameter("serial",firebase.getSerial());
		try{
			return (Firebase)query.getSingleResult();
		}catch(Exception e){
			//e.printStackTrace();
			return null;
		}
	}
	
	
	public Firebase findByCliente(Cliente cliente) {
		Query query = getEm().createQuery("select o from Firebase o where o.usuario.id=:usuarioId");
		query.setParameter("usuarioId",cliente.getUsuario().getId());
		try{
			return (Firebase)query.getSingleResult();
		}catch(Exception e){
			//e.printStackTrace();
			return null;
		}
	}
	
	
	public Firebase findByUsuario(Usuario usuario) {
		Query query = getEm().createQuery("select o from Firebase o where o.usuario.id=:usuarioId");
		query.setParameter("usuarioId",usuario.getId());
		try{
			return (Firebase)query.getSingleResult();
		}catch(Exception e){
			//e.printStackTrace();
			return null;
		}
	}


	

}
