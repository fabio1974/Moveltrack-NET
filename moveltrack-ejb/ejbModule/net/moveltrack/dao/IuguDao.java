package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Chip;
import net.moveltrack.domain.Iugu;
import net.moveltrack.domain.MBoletoStatus;

@Stateless
@SuppressWarnings("serial")
public class IuguDao extends DaoBean<Iugu>{

	public IuguDao() {
		
	}
	
	

	public Iugu findSemImagemCodigoDeBarras(){
		Query query = getEm().createQuery("select o.iugu from MBoleto o where o.iugu.invoiceId is not null and o.iugu.codigoBarrasImagem is null and o.situacao=:situacao");
		query.setMaxResults(1);
		query.setParameter("situacao",MBoletoStatus.EMITIDO);
		try{
			return (Iugu)query.getSingleResult();
		}catch(Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}

/*	public Chip findByNumero(String numero) {
		Query query = getEm().createQuery("select o from Chip o where o.numero =:numero");
		query.setParameter("numero",numero);
		try{
			return (Chip)query.getSingleResult();	
		}catch(Exception e){
			return null;
		}
	}
	
	public Chip findByIccid(String iccid) {
		Query query = getEm().createQuery("select o from Chip o where o.iccid =:iccid");
		query.setParameter("iccid",iccid);
		try{
			return (Chip)query.getSingleResult();	
		}catch(Exception e){
			return null;
		}
	}*/
	
	



}
