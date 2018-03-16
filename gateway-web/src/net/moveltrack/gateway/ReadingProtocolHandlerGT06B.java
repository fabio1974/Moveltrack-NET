package net.moveltrack.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;
import java.util.GregorianCalendar;

import net.moveltrack.domain.Location;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.gateway.utils.Constantes;
import net.moveltrack.gateway.utils.Utils;

public class ReadingProtocolHandlerGT06B extends ReadingProtocolHandler {

	boolean DEBUG_MODE = Constantes.DEBUG_GT06B;
	
	public ReadingProtocolHandlerGT06B()  {
	}
	
	
	@Override
	public void startReading(Socket clientSocket) throws IOException {

		Utils.log(DEBUG_MODE," START READING PROTOCOL GT06-B");
		String completeMessage="";
		int readByte = 0; 
		InputStream inputStream = clientSocket.getInputStream();

		while ((readByte=inputStream.read()) != -1){
			char readChar = (char) readByte;
			completeMessage += readChar;
			if (readChar==')'){
				if(completeMessage.contains("HSO")){
				}else if(completeMessage.contains("BP")){
					trataLocationFromBP(completeMessage);
				}else if(completeMessage.contains("BR")){
					trataLocationFromBR(completeMessage);
				}else if(completeMessage.contains("BO")){
					trataLocationFromBO(completeMessage);
				}
				completeMessage = "";
			}
		} 		     
	}

	private void trataLocationFromBO(String message) {
		Utils.log(DEBUG_MODE," BO - "+message);
	}

	private void trataLocationFromBR(String m) {
		Utils.log(DEBUG_MODE," BR -> "+m);
		//(088046277377BR00130815A0344.5679S03831.3962W000.0184132224.7900000000L00000000
		GpsCommand gc = new GpsCommand();
		try{
			gc.setImei("3541"+m.substring(2,m.indexOf("BR")));
			gc.setDatetime(m.substring(m.indexOf("BR")+4,m.indexOf("A")));
			gc.setNs(m.indexOf("S")!=-1?"S":"N");
			gc.setLat(m.substring(m.indexOf("A")+1,m.indexOf(gc.getNs())));
			gc.setEw(m.indexOf("W")!=-1?"W":"E");
			gc.setLon(m.substring(m.indexOf(gc.getNs())+1,m.indexOf(gc.getEw())));
			
			m = m.substring(m.indexOf(gc.getEw())+1);
			
			gc.setSpeed(m.substring(0,5));
			gc.setType("BR");
			gc.setTime(m.substring(5,14));
			
			Utils.log(DEBUG_MODE," - BR Montado "+gc.toString());
			insertPoinOnDatabase(gc);
			
		}catch(StringIndexOutOfBoundsException ex){
			Utils.log(DEBUG_MODE,"Message:"+ex.getMessage());
		}catch(Exception e){
			Utils.log(DEBUG_MODE,"Message:"+e.getStackTrace().toString());
			//e.printStackTrace();
		}
	}


	private void trataLocationFromBP(String m) {
		Utils.log(DEBUG_MODE," BP - "+m);
		//(088046277377BP05000088046277377130815A0344.5679S03831.3962W000.0184125224.7900000000L00000000
		                                  
		GpsCommand gc = new GpsCommand();
		try{
			gc.setImei("3541"+m.substring(m.indexOf("BP")+8,m.indexOf("BP")+19));
			gc.setDatetime(m.substring(m.indexOf("A")-6,m.indexOf("A")));
			
			m = m.substring(m.indexOf("A")+1); //0344.5679S03831.3962W000.0184125224.7900000000L00000000
			
			gc.setNs(m.indexOf("S")!=-1?"S":"N");
			gc.setLat(m.substring(0,m.indexOf(gc.getNs())));
			gc.setEw(m.indexOf("W")!=-1?"W":"E");
			gc.setLon(m.substring(m.indexOf(gc.getNs())+1,m.indexOf(gc.getEw())));
			
			m = m.substring(m.indexOf(gc.getEw())+1);
			
			gc.setSpeed(m.substring(0,5));
			gc.setType("BP");
			gc.setTime(m.substring(5,14));
			
			insertPoinOnDatabase(gc);
			
			Utils.log(DEBUG_MODE," - BP Montado"+gc.toString());
		}catch(StringIndexOutOfBoundsException ex){
			Utils.log(DEBUG_MODE,"Message:"+ex.getMessage());
		}catch(Exception e){
			Utils.log(DEBUG_MODE,"Message:"+e.getStackTrace().toString());
		}
		
	}


	private void insertPoinOnDatabase(GpsCommand gc) {
		try{
			Location loc = new Location();
			//BR Montado ->354188046277377 - BR - 130816 - 0348.1676 - S - S03830.2400 - W - 030.4 - 103746153<-
			loc.setImei(gc.getImei());
			
			String dt = gc.getDatetime(); //130816
			String t = gc.getTime(); // 103746153
			
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.set(2000+Integer.parseInt(dt.substring(0,2)),//ano
					Integer.parseInt(dt.substring(2,4))-1,//m�s
					Integer.parseInt(dt.substring(4,6)),//dia
					Integer.parseInt(t.substring(0,2)), //hora
					Integer.parseInt(t.substring(2,4)),//min
					Integer.parseInt(t.substring(4,6)));//seg
			calendar.set(GregorianCalendar.MILLISECOND,Integer.parseInt(t.substring(6,9)));//miliseg
			calendar.add(GregorianCalendar.HOUR_OF_DAY,-3);//<- a porra do rastreador manda sempre a hora com Time Zone = 0, mesmo setando no SMS
			
			String lat = gc.getLat();
			String lon = gc.getLon();
			float latitude = Float.parseFloat(lat.substring(0,2)) + Float.parseFloat(lat.substring(2))/60;
			float longitude = Float.parseFloat(lon.substring(0,3)) + Float.parseFloat(lon.substring(3))/60;
			
			Date dateLoc = calendar.getTime();
			loc.setDateLocation(dateLoc);
			loc.setDateLocationInicio(dateLoc);
			loc.setLatitude(gc.getNs().equals("N")?latitude:(-1)*latitude);
			loc.setLongitude(gc.getEw().equals("E")?longitude:(-1)*longitude);
			loc.setVelocidade(Float.parseFloat(gc.getSpeed()));
			loc.setComando(gc.getType());
			
			saveLocation(loc,ModeloRastreador.GT06B);
			
		}catch(StringIndexOutOfBoundsException ex){
			//System.out.println("Message:"+ex.getMessage());
			Utils.log(DEBUG_MODE,"Message:"+ex.getStackTrace().toString());
		}catch(Exception e){
			Utils.log(DEBUG_MODE,"Message:"+e.getStackTrace().toString());
		}
	}

}
