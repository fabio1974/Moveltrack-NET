package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.ContratoStatus;
import net.moveltrack.domain.Usuario;

@Stateless
@SuppressWarnings("serial")
public class ContratoDao extends DaoBean<Contrato>{


	public ContratoDao() { }

	public List<Contrato> findByTerm(String term){
		List<Contrato> list = new ArrayList<Contrato>();
		Query query = getEm().createQuery("select o from Contrato o where UPPER(o.cliente.nome) like '%"+term.toUpperCase()+"%' order by o.cliente.nome");
		try{
			list = (List<Contrato>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	
	public List<Contrato> findAtivos() {
		List<Contrato> list = new ArrayList<Contrato>();
		Query query = getEm().createQuery("select o from Contrato o where o.status=:status");
		query.setParameter("status",ContratoStatus.ATIVO);
		try{
			list = (List<Contrato>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}


	
	
	
	public Contrato findByCpf(String cpf){
		Query query = getEm().createQuery("select o from Contrato o where o.cliente.cpf=:cpf");
		query.setParameter("cpf",cpf);
		try{
			return (Contrato)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	
	public Contrato findByUsuario(Usuario usuario){
		Query query = getEm().createQuery("select o from Contrato o where o.cliente.usuario.id=:usuarioId");
		query.setParameter("usuarioId",usuario.getId());
		try{
			return (Contrato)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	
	
	public Contrato findByCnpj(String cnpj){
		Query query = getEm().createQuery("select o from Contrato o where o.cliente.cnpj:=cnpj");
		query.setParameter("cnpj",cnpj);
		try{
			return (Contrato)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}


	public Contrato findByClienteId(int clienteId) {
		Query query = getEm().createQuery("select o from Contrato o where o.cliente.id=:clienteId");
		query.setParameter("clienteId",clienteId);
		try{
			return (Contrato)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	public Contrato findByClienteStatus(Cliente cliente,ContratoStatus contratoStatus) {
		Query query = getEm().createQuery("select o from Contrato o where o.cliente.id=:clienteId and status=:status");
		query.setParameter("clienteId",cliente.getId());
		query.setParameter("status",contratoStatus);
		try{
			return (Contrato)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	


	public Contrato findByNomeCliente(String nome) {
		Query query = getEm().createQuery("select o from Contrato o where o.cliente.nome=:nome");
		query.setParameter("nome",nome);
		try{
			return (Contrato)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	public Contrato findByCliente(Cliente cliente) {
		Query query = getEm().createQuery("select o from Contrato o where o.cliente.id=:clienteId");
		query.setParameter("clienteId",cliente.getId());
		try{
			return (Contrato)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	public Object findByNumeroContrato(String numeroContrato) {
		Query query = getEm().createQuery("select o from Contrato o where o.numeroContrato=:numeroContrato");
		query.setParameter("numeroContrato",numeroContrato);
		try{
			return (Contrato)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}




}
