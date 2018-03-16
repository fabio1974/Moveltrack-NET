package net.moveltrack.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.GregorianCalendar;

import net.moveltrack.domain.Location;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.gateway.utils.Constantes;
import net.moveltrack.gateway.utils.Utils;

public class ReadingProtocolHandlerXT009 extends ReadingProtocolHandler {

	boolean DEBUG_MODE = Constantes.DEBUG_XT009;
	
	public ReadingProtocolHandlerXT009() {
	}
	
	@Override
	public void startReading(Socket socket) throws IOException {

		Utils.log(DEBUG_MODE," START READING NEW PROTOCOL XT009");
		String linha="";
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		while ((linha = stdIn.readLine()) != null) {
			if(linha.length()>0){
				//130826002308,+558592340623,GPRMC,032308.000,A,0344.5662,S,03831.3965,W,0.00,250.60,260813,,,A*60,F,, imei:863070018467471,09,31.0,F:3.86V,0,140,16712,724,05,0C71,2CF2
				if(linha.contains("GPRMC"))
					insertPoinOnDatabase(linha,"GPRMC");
				//lat: 22.566923 long: 114.051331 speed: 90.00 28/11/10 03:33 F:4.32V,0,Signal:F ACCstart imei:359587011016261 05 41.5 460 01 2533 720B
				else if(linha.contains("ACCstop"))
					if(DEBUG_MODE)
						System.out.println(linha);
				//insertPoinOnDatabase(linha,"ACCstop");
					else if(linha.contains("ACCstart"))
						if(DEBUG_MODE)
							System.out.println(linha);
				//insertPoinOnDatabase(linha,"ACCstart");
			}
		}
	}

	private void insertPoinOnDatabase(String linha,String type) {

		GpsCommand gc = new GpsCommand();
		String[] quebra =  linha.split(",");
		
		try{
			gc.setImei(quebra[17]);
			gc.setType(type);
			gc.setDatetime(quebra[0]);
			gc.setX1(quebra[18]);
			gc.setF(quebra[15]);
			//gc.setTime(quebra[5]);
			gc.setA(quebra[4]);
			gc.setLat(quebra[5]);
			gc.setNs(quebra[6]);
			gc.setLon(quebra[7]);
			gc.setEw(quebra[8]);
			gc.setSpeed(quebra[9]);
			gc.setX2(quebra[24]);
			
			Utils.log(DEBUG_MODE,gc.toString());
			
			Location loc = new Location();
			loc.setImei(gc.getImei().substring(" imei:".length()));
			
			String dt = gc.getDatetime();//130826002308
			String t = gc.getTime();
			
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.set(2000+Integer.parseInt(dt.substring(0,2)),
					Integer.parseInt(dt.substring(2,4))-1,
					Integer.parseInt(dt.substring(4,6)),
					Integer.parseInt(dt.substring(6,8)),
					Integer.parseInt(dt.substring(8,10)),
					Integer.parseInt(dt.substring(10,12)));
			
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
			loc.setSatelites(Integer.parseInt(gc.getX1()));
			loc.setMcc(Integer.parseInt(gc.getX2()));
			
			saveLocation(loc,ModeloRastreador.XT009);
			
		}catch(StringIndexOutOfBoundsException ex){
			Utils.log(DEBUG_MODE,"Message:"+ex.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			Utils.log(DEBUG_MODE,"Message:"+e.getStackTrace().toString());
		}
	}
}
