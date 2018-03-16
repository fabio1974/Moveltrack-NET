/*
 * AgenteDAO.java
 *
 * Created on 18 de Setembro de 2007, 15:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.moveltrack.gateway.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.moveltrack.domain.Sms;
import net.moveltrack.domain.SmsStatus;
import net.moveltrack.domain.SmsTipo;



/**
 * 
 * @author fabio.barros
 */
public class SmsDAO {

	
	private static final String INSERT = "insert into Sms (" +
											" celular," +
											" dataUltimaAtualizacao," +
											" imei," +
											" mensagem," +
											" status," +
											" tipo)"+
											" values (?,?,?,?,?,?)";
	
	private static SmsDAO instance;

	public static SmsDAO getInstance() {
		if (instance == null) {
			instance = new SmsDAO();
		}
		return instance;
	}

	private SmsDAO() {
		super();
	}



	
	
	public List<Sms> getWaitingPowerCommands() {
		List<Sms> smss = new ArrayList<Sms>();
		Sms sms=null;
		int count=0;
		Connection conn = Conexao.getMovelTrackConnection();
		PreparedStatement pstm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE,-1);
			Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
			
			sql =" select max(id) as id,imei from Sms "+
					" where dataUltimaAtualizacao > '"+timestamp+"' "
					+ " and (tipo = 'BLOQUEIO' or tipo = 'DESBLOQUEIO') and status = '"+SmsStatus.ESPERANDO_SOCKET+"' group by imei ";
			
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery(sql);
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
				rs.close();
				pstm.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return smss.size()>0?smss:null;
	}
	
	
	private Sms findSmsById(long id) {
		Sms sms = null;
		ResultSet rs = null;
		Statement pstm = null;
		Connection conn = Conexao.getMovelTrackConnection();
		try {
			String sql =" select * from Sms where id="+id;
			pstm = conn.createStatement();
			rs = pstm.executeQuery(sql);
			if(rs.next()){
				sms = new Sms();
				sms.setId(rs.getInt("id"));
				sms.setCelular(rs.getString("celular"));
				sms.setDataUltimaAtualizacao(rs.getTimestamp("dataUltimaAtualizacao"));
				sms.setImei(rs.getString("imei"));
				sms.setMensagem(rs.getString("mensagem"));
				sms.setStatus(SmsStatus.valueOf(rs.getString("status")));
				sms.setTipo(SmsTipo.valueOf(rs.getString("tipo")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
					rs.close();
					pstm.close();
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sms;
	}
	
	
	

	public void insert(Sms sms) {
		Connection conn = Conexao.getMovelTrackConnection();
		try{
			PreparedStatement pSta = conn.prepareStatement(INSERT);
			setParametros(pSta,sms);
			pSta.executeUpdate();
			pSta.close();
		}catch(Exception e){
				e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private  void setParametros(PreparedStatement pSta, Sms sms) {
		try {
			pSta.setString(1,sms.getCelular());
			Timestamp t = sms.getDataUltimaAtualizacao()!=null?(new Timestamp(sms.getDataUltimaAtualizacao().getTime())):null;
			pSta.setTimestamp(2,t);
			pSta.setString(3,sms.getImei());
			pSta.setString(4,sms.getMensagem());
			pSta.setString(5,sms.getStatus().name());
			pSta.setString(6,sms.getTipo().name());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	public boolean updateStatusTo(Sms sms,SmsStatus smsStatus) {
			Connection conn = Conexao.getMovelTrackConnection();
			PreparedStatement pSta = null;
			boolean r = false;
			try{
				
				String sql = "update Sms set status = '"+smsStatus+"' where id = "+sms.getId();
		
				pSta = conn.prepareStatement(sql);
				
				r = pSta.executeUpdate() == 0;
			}catch(Exception e){
					e.printStackTrace();
			}finally{
				try {
					pSta.close();
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return r;
	}	

}