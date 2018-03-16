package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Carne;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.domain.TipoDeCobranca;


@Stateless
@SuppressWarnings("serial")
public class MBoletoDao extends DaoBean<MBoleto>{


	
	public MBoletoDao() { }


    
	public synchronized String getProximoNossoNumero(TipoDeCobranca tipoDeCobranca) {
			Query query = getEm().createQuery("select max(substring(o.nossoNumero,3)) from MBoleto o ");
			long proximo = 0;
			try{
				String ultimo = (String)query.getSingleResult();
				proximo = ultimo!=null?(Long.parseLong(ultimo.substring(2))+1):1;
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
			return (tipoDeCobranca==TipoDeCobranca.COM_REGISTRO?14:24)+String.format("%015d",proximo);
	}


	public List<MBoleto> findByRemessa(String numero) {
		List<MBoleto> list = new ArrayList<MBoleto>();
		Query query = getEm().createQuery("select o from MBoleto o where o.remessa.numero =:numero");
		query.setParameter("numero",numero);
		try{
			list = (List<MBoleto>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public List<MBoleto> findNaoCanceladosByRemessa(String numero) {
		List<MBoleto> list = new ArrayList<MBoleto>();
		Query query = getEm().createQuery("select o from MBoleto o where o.remessa.numero =:numero and situacao<>:situacao and situacao<>:situacao2");
		
		query.setParameter("numero",numero);
		query.setParameter("situacao",MBoletoStatus.CANCELADO);
		query.setParameter("situacao2",MBoletoStatus.PAGAMENTO_EFETUADO);

		try{
			list = (List<MBoleto>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	

	public List<MBoleto> findByCarne(Carne carne) {
		List<MBoleto> list = new ArrayList<MBoleto>();
		Query query = getEm().createQuery("select o from MBoleto o where o.carne.id =:carneId");
		query.setParameter("carneId",carne.getId());
		try{
			list = (List<MBoleto>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<MBoleto> findEmitidosByCarne(Carne carne) {
		List<MBoleto> list = new ArrayList<MBoleto>();
		Query query = getEm().createQuery("select o from MBoleto o where o.contrato.id =:contratoId and o.dataVencimento>:referencia and o.situacao=:situacao order by o.dataVencimento");
		query.setParameter("contratoId",carne.getContrato().getId());
		query.setParameter("referencia",carne.getDataReferencia());
		query.setParameter("situacao",MBoletoStatus.EMITIDO);
		try{
			list = (List<MBoleto>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}



	public MBoleto findByNossoNumero(String nossoNumero) {
		Query query = getEm().createQuery("select o from MBoleto o where o.nossoNumero=:nossoNumero");
		query.setParameter("nossoNumero",nossoNumero);
		try{
			return (MBoleto)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	

	public MBoleto findEmitidoMaisAntigoSemInvoce() {
		Query query = getEm().createQuery("select o from MBoleto o where o.situacao=:situacao and o.iugu is null order by o.dataEmissao desc");
		query.setParameter("situacao",MBoletoStatus.EMITIDO);
		query.setMaxResults(1);
		try{
			return (MBoleto)query.getSingleResult();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}




	public void venceBoletos() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		String sql = " update MBoleto m set m.situacao=:situacao1 where m.dataVencimento<:comecoDeHoje and m.situacao=:situacao2 ";
		Query q = getEm().createQuery(sql);
		q.setParameter("situacao1", MBoletoStatus.VENCIDO);
		q.setParameter("comecoDeHoje", calendar.getTime());
		q.setParameter("situacao2", MBoletoStatus.EMITIDO);
		q.executeUpdate();
		
	}



	public List<MBoleto> findByClienteStatus(Cliente cliente, MBoletoStatus situacao) {
		List<MBoleto> list = new ArrayList<MBoleto>();
		Query query = getEm().createQuery("select o from MBoleto o where o.contrato.cliente.id =:clienteId and o.situacao=:situacao order by o.dataVencimento");
		query.setParameter("clienteId",cliente.getId());
		query.setParameter("situacao",situacao);
		try{
			list = (List<MBoleto>)query.getResultList();
		}catch(Exception e){
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		return list;
	}



	public List<MBoleto> getBoletosAPagar(Cliente cliente) {
		List<MBoleto> list = new ArrayList<MBoleto>();
		Query query = getEm().createQuery("select o from MBoleto o where o.contrato.cliente.id =:clienteId and o.situacao=:situacao and o.dataVencimento>:now ");
		query.setParameter("clienteId",cliente.getId());
		query.setParameter("situacao",MBoletoStatus.EMITIDO);		
		query.setParameter("now",new Date());
		try{
			list = (List<MBoleto>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}



	public List<MBoleto> findByStatusInicioFim(MBoletoStatus situacao, Date inicio, Date fim) {
		List<MBoleto> list = new ArrayList<MBoleto>();
		Query query = getEm().createQuery("select o from MBoleto o where o.situacao=:situacao and o.dataRegistroPagamento>=:inicio and o.dataRegistroPagamento<=:fim order by o.dataRegistroPagamento");
		query.setParameter("situacao",situacao);		
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			list = (List<MBoleto>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	
	public double sumByStatusInicioFim(MBoletoStatus situacao, Date inicio, Date fim) {
		Query query = getEm().createQuery("select SUM(o.valor) from MBoleto o where o.situacao=:situacao and o.dataRegistroPagamento>=:inicio and o.dataRegistroPagamento<=:fim order by o.dataRegistroPagamento");
		query.setParameter("situacao",situacao);		
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			return (Double)query.getSingleResult();
		}catch(Exception e){
			return 0;
		}
	}



	public MBoleto findByIuguInvoiceId(String invoiceId) {
		Query query = getEm().createQuery("select o from MBoleto o where o.iugu.invoiceId=:invoiceId");
		query.setParameter("invoiceId",invoiceId);
		try{
			return (MBoleto)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}



}
