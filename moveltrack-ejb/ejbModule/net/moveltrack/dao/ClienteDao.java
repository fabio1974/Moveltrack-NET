package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ContratoStatus;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.domain.Usuario;

@Stateless
@SuppressWarnings("serial")
public class ClienteDao extends DaoBean<Cliente>{


	public ClienteDao() { }


	public List<Cliente> findByTerm(String term){
		List<Cliente> list = new ArrayList<Cliente>();
		Query query = getEm().createQuery("select o from Cliente o where UPPER(o.nome) like '%"+term.toUpperCase()+"%' order by o.nome");
		try{
			list = (List<Cliente>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Cliente> findParaCobranca(){
		List<Cliente> list = new ArrayList<Cliente>();
		Query query = getEm().createQuery("select o from Cliente o order by o.nome");
		try{
			list = (List<Cliente>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	
	public List<Cliente> findParaLembrete() {
		List<Cliente> list = new ArrayList<Cliente>();
		Query query = getEm().createQuery("select o from Cliente o order by o.nome");
		try{
			list = (List<Cliente>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	

	
	
	
	public Cliente findByNome(String nome) {
		Query query = getEm().createQuery("select o from Cliente o where o.nome =:nome");
		query.setParameter("nome",nome);
		try{
			return (Cliente)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	
	public Cliente findByCpf(String cpf){
		Query query = getEm().createQuery("select o from Cliente o where o.cpf=:cpf");
		query.setParameter("cpf",cpf);
		try{
			return (Cliente)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	

	
	
	public Cliente findByCnpj(String cnpj){
		Query query = getEm().createQuery("select o from Cliente o where o.cnpj=:cnpj");
		query.setParameter("cnpj",cnpj);
		try{
			return (Cliente)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}


	public net.moveltrack.domain.Cliente findByUsuarioNome(String nomeUsuario) {
		Query query = getEm().createQuery("select o from Cliente o where o.usuario.nomeUsuario=:nomeUsuario");
		query.setParameter("nomeUsuario",nomeUsuario);
		try{
			return (Cliente)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	public net.moveltrack.domain.Cliente findByUsuario(Usuario usuario) {
		Query query = getEm().createQuery("select o from Cliente o where o.usuario.id=:usuarioId");
		query.setParameter("usuarioId",usuario.getId());
		try{
			return (Cliente)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}


	public List<Cliente> findAlarmeBateria() {
		List<Cliente> list = new ArrayList<Cliente>();
		Query query = getEm().createQuery("select o from Cliente o where o.clienteConfig is not null and o.clienteConfig.alarmeBateria=:alarmeBateria");
		query.setParameter("alarmeBateria",true);
		try{
			list = (List<Cliente>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	public List<Cliente> findAlarmeVelocidade() {
		List<Cliente> list = new ArrayList<Cliente>();
		Query query = getEm().createQuery("select o from Cliente o where o.clienteConfig is not null and o.clienteConfig.alarmeVelocidade=:alarmeVelocidade");
		query.setParameter("alarmeVelocidade",true);
		try{
			list = (List<Cliente>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}

	
	
	@SuppressWarnings("unchecked")
	public List<Integer> findRelatorioDistanciaDiaria(){
		
		List<Integer> ids = null; 
		
		String sql = "	select p.id from usuario u" + 
				"	inner join pessoa p on p.usuario_id = u.id" + 
				"	inner join usuario_permissao up on up.usuario_id = u.id" + 
				"	inner join permissao pe on pe.id = up.permissoes_id" + 
				"	where pe.descricao = 'RELATORIO_DISTANCIA_PERCORRIDA'" + 
				"	and p.status = 'ATIVO'" + 
				"" + 
				"	UNION" + 
				"" + 
				"	select p.id from usuario u" + 
				"	inner join pessoa p on p.usuario_id = u.id" + 
				"	inner join perfil pf on u.perfil_id = pf.id" + 
				"	inner join perfil_permissao pp on pf.id = pp.Perfil_id" + 
				"	inner join permissao pe on pe.id = pp.permissoes_id" + 
				"	where pe.descricao = 'RELATORIO_DISTANCIA_PERCORRIDA'" + 
				"	and p.status = 'ATIVO'";
	
		Query query = getEm().createNativeQuery(sql);
		try{
			ids = (List<Integer>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return ids;
	}






}
