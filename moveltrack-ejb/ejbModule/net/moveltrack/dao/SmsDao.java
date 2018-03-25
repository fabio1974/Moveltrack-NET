package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import net.moveltrack.domain.Sms;
import net.moveltrack.domain.SmsStatus;

@Stateless
@SuppressWarnings("serial")
public class SmsDao extends DaoBean<Sms>{

	public SmsDao() { }

	boolean DEBUG = false;
	
	private void log(String message) {
		if(DEBUG)
			System.out.println(message);
	}
	
	public List<Sms> getWaitingPowerCommands() {
		
		try{
			List<Sms> smss = new ArrayList<Sms>();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE,-1);

			String sql =" select max(s.id),s.imei from Sms s "+
					" where s.dataUltimaAtualizacao >:dataUltimaAtualizacao "
					+ " and (s.tipo = 'BLOQUEIO' or s.tipo = 'DESBLOQUEIO') and s.status=:status group by s.imei ";

			Query query = getEm().createQuery(sql);
			query.setParameter("dataUltimaAtualizacao",calendar.getTime());
			query.setParameter("status",SmsStatus.ESPERANDO_SOCKET);
			List<Object[]>  objects = (List<Object[]>)query.getResultList();

			log(sql);
			log(calendar.getTime().toString());
			log(""+SmsStatus.ESPERANDO_SOCKET);

			if(objects!=null && objects.size()>0) {
				for (Object[] object : objects) {
					Sms sms = findById((Integer)object[0]);
					smss.add(sms);
					log("Pegando Sms do Tipo "+sms.getTipo() + " para disparo de comando por GPRS. Imei:"+sms.getImei());
				}
			}else{
				log("Zero para disparo de comando por GPRS.");
			}
			return smss.size()>0?smss:null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
    public Sms getUltimoSmsDoChip(String celular){
    	Sms sms = null;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MINUTE,-1);
		Query query = getEm().createQuery("select max(s.id) from Sms s where s.celular=:celular and s.dataUltimaAtualizacao >:data");
	    query.setParameter("celular",celular);
	    query.setParameter("data",c.getTime());
	    Integer id = (Integer)query.getSingleResult();
	    if(id!=null){
	    	sms = findById(id);
	    }	
    	return sms;
    }
    
    /**
     * 
     * @param status  Estatus do Sms
     * @param minutes Pega os Smss nos ultimos 'x' minutos
     * @param max     Quantidade máxima de registros 
     * @return
     */

	public List<Sms> findByStatusPastMinute(SmsStatus status, int minutes, int max) {
		try {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE,- 1 * minutes);
			Query q = getEm().createQuery("select s from Sms s where s.status=:status and s.dataUltimaAtualizacao >:data ");
			q.setParameter("status",status);
			q.setParameter("data",c.getTime());
			if(max>0)
				q.setMaxResults(max);
			return q.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	

    public Sms findLastByCelularStatusNoUltimoMinuto(String celular,SmsStatus status){
    	Sms sms = null;
    	try{
    		Calendar c = Calendar.getInstance();
    		c.setTime(new Date());
    		c.add(Calendar.MINUTE,-1);
    		Query query = getEm().createQuery("select max(s.id) from Sms s where s.celular=:celular and s.dataUltimaAtualizacao >:data and s.status=:status");
    		query.setParameter("celular",celular);
    		query.setParameter("data",c.getTime());
    		query.setParameter("status",status);
    		Integer id = (Integer)query.getSingleResult();
    		if(id!=null){
    			sms = findById(id);
    		}	
    	}catch(Exception e){
    		sms = null;
    	}
    	return sms;
    }	
	
/*	public List<Sms> getWaitingPowerCommands() {
		List<Sms> smss = new ArrayList<Sms>();
		Sms sms=null;
		Connection conn = Conexao.getMovelTrackConnection();
		try {
			Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
			
			String sql =" select max(id) as id,imei from sms "+
					" where data_ultima_atualizacao > '"+timestamp+"' "
					+ " and (tipo = 'BLOQUEIO' or tipo = 'DESBLOQUEIO') and status = '"+SmsStatus.ESPERANDO_SOCKET+"' group by imei ";
			
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery(sql);
			int count=0;
			while(rs.next()){
				sms = findSmsById(rs.getLong("id"));
				if(sms!=null) {
					smss.add(sms);
					count++;
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return smss.size()>0?smss:null;
	}
*/	
	

}
