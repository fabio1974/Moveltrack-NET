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

import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.VeiculoTipo;

/**
 * 
 * @author fabio.barros
 */
public class EquipamentoDAO {

	
	

	private static EquipamentoDAO instance;

	public static EquipamentoDAO getInstance() {
		if (instance == null) {
			instance = new EquipamentoDAO();
		}
		return instance;
	}

	private EquipamentoDAO() {
		super();
	}


	
	private int setParametros(PreparedStatement pSta, Location loc)	throws SQLException {
		int i = 1;
		
		return i;
	}	

/*	public boolean update(String imei, int comando) {
		Connection conn = Conexao.getMovelTrackConnection();
		boolean r = false;
		try{
			PreparedStatement pSta = conn.prepareStatement("update Equipamento set comando = ? where imei = ?");
			pSta.setInt(1,comando);
			pSta.setString(2,imei);
			r = pSta.executeUpdate() == 0;
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
*/


/*	public int getComando(String imei) {
		int comando = 0;
		Connection conn = Conexao.getMovelTrackConnection();
		try {
			PreparedStatement pstm = conn.prepareStatement("select comando from Equipamento where imei = ?");
			pstm.setString(1,imei);
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
				comando = rs.getInt("comando");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return comando;
	}*/
	
/*	public String getSenha(String imei) {
		String senha = "";
		Connection conn = Conexao.getMovelTrackConnection();
		try {
			PreparedStatement pstm = conn.prepareStatement("select senha from Equipamento where imei = ?");
			pstm.setString(1,imei);
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
				senha = rs.getString("senha");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return senha;
	}*/
	
	
	
	
/*	public int getAtrasoGmtByImei(String imei) {
		int atrasoGmt = 0;
		Connection conn = Conexao.getMovelTrackConnection();
		try {
			PreparedStatement pstm = conn.prepareStatement("select atrasoGmt from Equipamento where imei = ?");
			pstm.setString(1,imei);
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				atrasoGmt = rs.getInt("atrasoGmt");
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
		return atrasoGmt;
	}*/
	
	
	public Veiculo getVeiculoByEquipamento(int equipamentoId) {
		Veiculo veiculo = null;
		Connection conn = Conexao.getMovelTrackConnection();
		try {
			PreparedStatement pstm = conn.prepareStatement("select * from Veiculo where equipamento = ?");
			pstm.setLong(1,equipamentoId);
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				veiculo = new Veiculo();
				veiculo.setPlaca(rs.getString("placa"));
				veiculo.setMarcaModelo(rs.getString("marcaModelo"));
				veiculo.setId(rs.getInt("id"));
				//veiculo.setContrato(rs.getInt("proprietario"));
				veiculo.setTipo(VeiculoTipo.valueOf(rs.getString("tipo")));
				
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
		return veiculo;
	}
	
	
	public Cliente getClienteById(int clienteId) {
		Cliente cliente = null;
		Connection conn = Conexao.getMovelTrackConnection();
		try {
			PreparedStatement pstm = conn.prepareStatement("select * from Pessoa where id = ?");
			pstm.setLong(1,clienteId);
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				cliente = new Cliente();
				cliente.setNome(rs.getString("nome"));
				cliente.setEmail(rs.getString("email"));
				cliente.setEmailAlarme(rs.getString("emailAlarme"));
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
		return cliente;
	}
	
	
	
	public void salvaAtraso(String imei, int atrasoGmt) {
		Connection conn = Conexao.getMovelTrackConnection();
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement("update equipamento set atrasoGmt="+atrasoGmt+" where imei='"+imei+"'");
			pstm.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public int getAtraso(String imei) {
		Connection conn = Conexao.getMovelTrackConnection();
		PreparedStatement pstm;
		try {
			pstm = conn.prepareStatement("select atrasoGmt from equipamento where imei='"+imei+"'");
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) 
				return rs.getInt("atrasoGmt");
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	
	
	
	
	


	public Cliente getClienteByVeiculo(Veiculo veiculo) {
		Cliente cliente = null;
		Connection conn = Conexao.getMovelTrackConnection();
		try {
			PreparedStatement pstm = conn.prepareStatement("select p.* from Pessoa as p, Contrato as c, Veiculo as v " 
					+ " where "
					+ "c.cliente_id = p.id and v.contrato_id = c.id"
					+ "v.id = ?");
			
			pstm.setLong(1,veiculo.getId());
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				cliente = new Cliente();
				cliente.setNome(rs.getString("nome"));
				cliente.setEmail(rs.getString("email"));
				cliente.setEmailAlarme(rs.getString("emailAlarme"));
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
		return cliente;
	}	
}	