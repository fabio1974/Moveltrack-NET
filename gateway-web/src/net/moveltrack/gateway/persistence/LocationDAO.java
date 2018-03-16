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
import java.sql.SQLException;
import java.sql.Timestamp;

import net.moveltrack.domain.Location;

/**
 * 
 * @author fabio.barros
 */
public class LocationDAO {


	/** Creates a new instance of AgenteDAO */

	private static LocationDAO instance;

	public static LocationDAO getInstance() {
		if (instance == null) {
			instance = new LocationDAO();
		}
		return instance;
	}

	private LocationDAO() {
		super();
	}


	public  int insert(Location loc,String tabela) {
		Connection conn = Conexao.getMovelTrackConnection();
		int r = 0;
		try{
			
			String INSERT = "insert into "+tabela+" (" ;
			INSERT+= " mcc," +
					" satelites," +
					" comando," +
					" dateLocation," +
					" dateLocationInicio," +
					" imei," +
					" latitude,"+
					" longitude,"+
					" version,"+
					" velocidade," +
					" bloqueio," +
					" gps," +
					" gsm," +
					" sos," +
					" battery," +
					" volt," +
					" ignition," +
					" alarm," +
					" alarmType)"+											
					
					" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			PreparedStatement pSta = conn.prepareStatement(INSERT);
			setParametros(pSta, loc);
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

		return r;
	}
	
	
	private int setParametros(PreparedStatement pSta, Location loc)	throws SQLException {
		int i = 1;
		pSta.setInt(i++, loc.getMcc());
		pSta.setInt(i++, loc.getSatelites());
		pSta.setString(i++, loc.getComando());
		pSta.setTimestamp(i++,new Timestamp(loc.getDateLocation().getTime()));
		pSta.setTimestamp(i++,new Timestamp(loc.getDateLocationInicio().getTime()));
		pSta.setString(i++, loc.getImei());
		pSta.setDouble(i++, loc.getLatitude());
		pSta.setDouble(i++, loc.getLongitude());
		pSta.setInt(i++,loc.getVersion());
		pSta.setDouble(i++,loc.getVelocidade());
		pSta.setString(i++, loc.getBloqueio());
		pSta.setString(i++, loc.getGps());
		pSta.setString(i++, loc.getGsm());
		pSta.setString(i++, loc.getSos());
		pSta.setString(i++, loc.getBattery());
		pSta.setString(i++, loc.getVolt());
		pSta.setString(i++, loc.getIgnition());
		pSta.setString(i++, loc.getAlarm());
		pSta.setString(i++, loc.getAlarmType());

		return i;
	}

	public void remove(Location loc) {
		Connection conn = Conexao.getMovelTrackConnection();
		try{
			PreparedStatement pSta = conn.prepareStatement("delete from location where id="+loc.getId());
			setParametros(pSta,loc);
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
	
	public void remove2(Location loc) {
		Connection conn = Conexao.getMovelTrackConnection();
		try{
			PreparedStatement pSta = conn.prepareStatement("delete from location2 where id="+loc.getId());
			setParametros(pSta,loc);
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



}