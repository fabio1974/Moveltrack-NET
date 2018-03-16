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
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



/**
 *
 * @author fabio.barros
 * 
 *   
	   Abre a conex�o com o banco de dados se existir;
	  
 */
public class Conexao {
    
    public static Connection getMovelTrackConnection(){
    	Connection connection = null;
    	InitialContext ctx;
		try {
			ctx = new InitialContext();
			DataSource db = (DataSource) ctx.lookup("java:/mysql/local/moveltrack");
	    	connection = db.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}	
    	return connection;
    }
    
    
 }
