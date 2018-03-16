package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.VeiculoStatus;
import net.moveltrack.domain.Viagem;

@Stateless
@SuppressWarnings("serial")
public class VeiculoDao extends DaoBean<Veiculo>{


	public VeiculoDao() { }

	
	@SuppressWarnings("unchecked")
	public List<Veiculo> findByPlacaTerm(String term){
		List<Veiculo> list = new ArrayList<Veiculo>();
		Query query = getEm().createQuery("select o from Veiculo o where UPPER(o.placa) like '%"+term.toUpperCase()+"%' order by o.placa");
		try{
			list = (List<Veiculo>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	
	
	public List<Veiculo> findByContrato(Contrato contrato){
		Query query = getEm().createQuery("select o from Veiculo o where o.contrato.id=:contratoId");
		query.setParameter("contratoId",contrato.getId());
		try{
			return (List<Veiculo>)query.getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
	
	public List<Veiculo> findByCliente(Cliente cliente){
		Query query = getEm().createQuery("select o from Veiculo o where o.contrato.cliente.id=:clienteId order by o.placa");
		query.setParameter("clienteId",cliente.getId());
		try{
			return (List<Veiculo>)query.getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
	
	public List<Veiculo> findAtivosByCliente(Cliente cliente){
		Query query = getEm().createQuery("select o from Veiculo o where o.contrato.cliente.id=:clienteId and o.status=:status order by o.placa");
		query.setParameter("clienteId",cliente.getId());
		query.setParameter("status",VeiculoStatus.ATIVO);
		
		try{
			return (List<Veiculo>)query.getResultList();
		}catch(Exception e){
			return null;
		}
	}



	public Veiculo findByEquipamento(String imei) {
		Query query = getEm().createQuery("select o from Veiculo o where o.equipamento.imei=:imei");
		query.setParameter("imei",imei);
		try{
			return (Veiculo)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}




	public Veiculo findByPlaca(String placa) {
		Query query = getEm().createQuery("select o from Veiculo o where o.placa=:placa");
		query.setParameter("placa",placa);
		try{
			return (Veiculo)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	public Veiculo findByPlacaStatus(String placa,VeiculoStatus status) {
		Query query = getEm().createQuery("select o from Veiculo o where o.placa=:placa and o.status=:status ");
		query.setParameter("placa",placa);
		query.setParameter("status",status);
		try{
			return (Veiculo)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	public Veiculo findByPlacaOtimizado(String placa) {
		try{
			if(placa.indexOf("-")<=0)
				placa = placa.substring(0,3)+"-"+placa.substring(3);
			Query query = getEm().createQuery("select o from Veiculo o where o.placa=:placa");
			query.setParameter("placa",placa);
			return (Veiculo)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	
	public Long getVeiculosAtivosCount(Contrato contrato) {
		Query query = getEm().createQuery("select count(o) from Veiculo o where o.contrato.id=:contratoId and o.status=:status");
		query.setParameter("contratoId",contrato.getId());
		query.setParameter("status",VeiculoStatus.ATIVO);
		try {
			return (Long)query.getSingleResult();
		} catch (Exception e) {
			return 0l;
		}
	}




	public net.moveltrack.domain.Veiculo findByClienteModeloSemPlaca(net.moveltrack.domain.Cliente cliente,String marcaModelo) {
		Query query = getEm().createQuery("select o from Veiculo o where o.contrato.cliente.id=:clienteId and o.marcaModelo=:marcaModelo");
		query.setParameter("clienteId",cliente.getId());
		query.setParameter("marcaModelo",marcaModelo);
		try {
			List<Veiculo> veiculos = (List<Veiculo>)query.getResultList();
			if(veiculos.size()>1)
				return veiculos.get(0);
			else if(veiculos.size()==1)
				return veiculos.get(0).getPlaca()!=null && veiculos.get(0).getPlaca().length()>2?null:veiculos.get(0);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}



}
