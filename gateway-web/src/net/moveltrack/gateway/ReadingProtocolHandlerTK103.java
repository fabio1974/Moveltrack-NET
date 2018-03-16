package net.moveltrack.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.GregorianCalendar;

import net.moveltrack.domain.Location;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.gateway.utils.Constantes;
import net.moveltrack.gateway.utils.Utils;

public class ReadingProtocolHandlerTK103 extends ReadingProtocolHandler {
	
	boolean DEBUG_MODE = Constantes.DEBUG_TK103;
	
	public ReadingProtocolHandlerTK103() {
	}
	
	@Override
	public void startReading(Socket socket) throws IOException {

		 InputStream inputStream = socket.getInputStream();
		 OutputStream outputStream = socket.getOutputStream();
		 PrintWriter pw = new PrintWriter(outputStream, true);
		 String completeMessage="";
	     int readByte = 0; 
	     String imei="";
	     
	     Utils.log(DEBUG_MODE," - START READING PROTOCOL TK103");
	     
	     while ((readByte=inputStream.read()) != -1){

	    	 char readChar = (char) readByte;
	    	 completeMessage += readChar;
	    	 
	    	 if (readChar==';'){
	    		 Utils.log(DEBUG_MODE," - "+completeMessage);
		        if(completeMessage.startsWith("##,imei:")){
		        	pw.println("LOAD");
		        	imei = completeMessage.substring("##,imei:".length(),"##,imei:".length()+15);
		        	Utils.log(DEBUG_MODE,"->"+imei+"<-");
		        	Utils.log(DEBUG_MODE," - ENVIOU LOAD!");
		        }else if(completeMessage.equals(imei+";")){
		        	pw.println("ON");
		        	Utils.log(DEBUG_MODE," - ENVIOU ON!");
		        }else {
		        	insertPoinOnDatabase(completeMessage);
		        	
		        }
	    		completeMessage = "";
	    	 }
	     } 	
	}
	


	private void insertPoinOnDatabase(String completeMessage) {

		GpsCommand gc = new GpsCommand();
		String[] quebra =  completeMessage.split(",");
		
		try{
			gc.setImei(quebra[0]);
			gc.setType(quebra[1]);
			gc.setDatetime(quebra[2]);
			gc.setX1(quebra[3]);
			gc.setF(quebra[4]);
			gc.setTime(quebra[5]);
			gc.setA(quebra[6]);
			gc.setLat(quebra[7]);
			gc.setNs(quebra[8]);
			gc.setLon(quebra[9]);
			gc.setEw(quebra[10]);
			gc.setSpeed(quebra[11]);
			gc.setX2(quebra[12]);
			
			//EntityManager em = EntityProviderUtil.get().getEntityManager(Location.class);
			Location loc = new Location();
			loc.setImei(gc.getImei().substring("imei:".length()));
			
			//sendComandToTracker(loc.getImei());
			
			String dt = gc.getDatetime();
			String t = gc.getTime();
			
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.set(2000+Integer.parseInt(dt.substring(0,2)),
					Integer.parseInt(dt.substring(2,4))-1,
					Integer.parseInt(dt.substring(4,6)),
					Integer.parseInt(dt.substring(6,8)),
					Integer.parseInt(dt.substring(8,10)),
					Integer.parseInt(t.substring(4,6)));
			
			calendar.set(GregorianCalendar.MILLISECOND,Integer.parseInt(t.substring(7,10)));
			String lat = gc.getLat();
			String lon = gc.getLon();
			float latitude = Float.parseFloat(lat.substring(0,2)) + Float.parseFloat(lat.substring(2))/60;
			float longitude = Float.parseFloat(lon.substring(0,3)) + Float.parseFloat(lon.substring(3))/60;
			
			Date dateLoc = calendar.getTime();
			loc.setDateLocation(dateLoc);
			loc.setDateLocationInicio(dateLoc);
			loc.setLatitude(gc.getNs().equals("N")?latitude:(-1)*latitude);
			loc.setLongitude(gc.getEw().equals("E")?longitude:(-1)*longitude);
			loc.setVelocidade(Float.parseFloat(gc.getSpeed())*3.6/1.852);
			loc.setComando(gc.getType());
			
			saveLocation(loc,ModeloRastreador.TK103A2);
			
		}catch(StringIndexOutOfBoundsException ex){
			Utils.log(DEBUG_MODE,"Message:"+ex.getMessage());
		}catch(Exception e){
			Utils.log(DEBUG_MODE,e.getStackTrace().toString());
		}
	}
	
	
	/*	private void sendComandToTracker(String imei) {
	try{
		int comando = EquipamentoDAO.getInstance().getComando(imei);
		if(comando==Comando.STOP_ENGINE){
			String senha = EquipamentoDAO.getInstance().getSenha(imei);
			String command = "Fix060s***n"+senha;
			Utils.log(DEBUG_MODE," - STOP_ENGINE"+command);
			pw.print(command);
			EquipamentoDAO.getInstance().update(imei, Comando.NOTHING);
		}
		if(comando==Comando.START_ENGINE){
			String senha = EquipamentoDAO.getInstance().getSenha(imei);
			String command = "Fix020s***n"+senha;
			Utils.log(DEBUG_MODE," - START_ENGINE"+command);
			pw.print(command);
			EquipamentoDAO.getInstance().update(imei, Comando.NOTHING);
		}
	}catch(Exception e){
		Utils.log(DEBUG_MODE,e.getStackTrace().toString());
	}
}*/

//Location lastLocation;
	

	
}
